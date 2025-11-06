package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.services.ImageService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageServiceImpl implements ImageService {

    private String uploadDir;
    private String accessSrc;

    public ImageServiceImpl(@Value("${app.upload.dir}") String uploadDir,  @Value("${app.access.src}") String accessSrc) {
        this.uploadDir = uploadDir;
        this.accessSrc = accessSrc;

        try{
            Files.createDirectories(Path.of(uploadDir));
        }
        catch (IOException e){
            System.out.println("Could not create directory: " + uploadDir);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String saveImage(MultipartFile file, String name) throws IOException {
        if(file.isEmpty() || file.getSize() == 0 || file.getContentType().isEmpty()) {
            throw new IllegalArgumentException("Can't save an empty file");
        }
        if(!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Can't save an image of type " + file.getContentType());
        }

        var fileName = name + '.' + FilenameUtils.getExtension(StringUtils.cleanPath(file.getOriginalFilename()));

        Path path = Paths.get(uploadDir, fileName);
        Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return accessSrc + uploadDir + fileName;
    }
}
