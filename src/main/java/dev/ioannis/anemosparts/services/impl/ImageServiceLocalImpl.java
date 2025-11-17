package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.repositories.ImageRepo;
import dev.ioannis.anemosparts.services.ImageService;
import dev.ioannis.anemosparts.services.SecurityService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.security.NoSuchAlgorithmException;

@Service
public class ImageServiceLocalImpl implements ImageService {

    private final SecurityService security;
    private final ImageRepo imageRepo;

    private final String accessSrc;

    public ImageServiceLocalImpl(SecurityService security, ImageRepo imageRepo, @Value("${app.access.src}") String accessSrc) {
        this.security = security;
        this.imageRepo = imageRepo;

        this.accessSrc = accessSrc;
    }

    @Override
    public String save(InputStream imageStream, String imageOriginalName, String imageContent, byte[] imageBytes) throws IOException {
        if(imageBytes.length == 0 || imageContent.isEmpty()) {
            throw new IllegalArgumentException("Can't save an empty file");
        }
        if(!imageContent.startsWith("image/")) {
            throw new IllegalArgumentException("Can't save an image of type " + imageContent);
        }

        try {
            String name = security.bytesToHash(imageBytes);

            var fileName = name + '.' + FilenameUtils.getExtension(StringUtils.cleanPath(imageOriginalName));

            if(imageRepo.exists(fileName)) return accessSrc + fileName;
            else return accessSrc + imageRepo.save(imageStream, fileName);

        } catch (NoSuchAlgorithmException e) {
            throw new IOException(e.getMessage());
        }
    }
}
