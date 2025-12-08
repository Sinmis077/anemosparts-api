package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.repositories.ImageRepo;
import dev.ioannis.anemosparts.repositories.PartImageRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrphanService {

    @Value("${app.upload.dir}")
    private String uploadDir;

    private final ImageRepo imageRepo;
    private final PartImageRepo partImageRepo;

    @Scheduled(cron = "0 0 0 * * *") // Every day at 12:00
    public void cleanUpOrphanImages() {
        log.info("Cleaning up orphan images");

        Pattern pattern = Pattern.compile("([^\\/]+$)");
        log.info("Set up the image pattern for name obtaining");

        Set<String> usedImages = new HashSet<String>(partImageRepo.findAll().stream().map(partImage -> {
            var source = partImage.getSource();
            Matcher matcher = pattern.matcher(source);

            if (matcher.find()) {
                return matcher.group();
            }

            return null;
        }).toList());

        log.debug("Found all images present in the database");

        try {
            Files.list(Paths.get(uploadDir)).forEach(image -> {
                var matcher = pattern.matcher(image.toString());
                if (matcher.find()) {
                    var imageName = matcher.group();
                    if(usedImages.stream().noneMatch(usedImage -> Objects.equals(usedImage, imageName))) {
                        try {
                            Files.deleteIfExists(image);
                        } catch (IOException e) {
                            log.error("Failed to delete orphan image: {}", e.getMessage());
                        }
                    }
                }
            });

            log.info("Successfully cleaned up orphan images");
        } catch (Exception e) {
            if(e instanceof IOException) {
                log.error("Failed to list images: {}", e.getMessage());
            }
            else {
                log.error("Orphan serviced unexpected error: {}", e.getMessage());
            }
        }
    }
}
