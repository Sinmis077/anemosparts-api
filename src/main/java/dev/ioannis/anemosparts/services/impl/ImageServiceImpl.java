package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.services.ImageService;
import dev.ioannis.anemosparts.services.SecurityService;
import lombok.AllArgsConstructor;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.naming.ServiceUnavailableException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.NoSuchAlgorithmException;

@Service
public class ImageServiceImpl implements ImageService {

    private SecurityService security;

    private String uploadDir;
    private String accessSrc;

    public ImageServiceImpl(@Value("${app.upload.dir}") String uploadDir,  @Value("${app.access.src}") String accessSrc, SecurityService security) {
        this.uploadDir = uploadDir;
        this.accessSrc = accessSrc;

        this.security = security;

        try{
            Files.createDirectories(Path.of(uploadDir));
        }
        catch (IOException e){
            System.out.println("Could not create directory: " + uploadDir);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String saveImage(MultipartFile file) throws IOException, ServiceUnavailableException {
        if(file.isEmpty() || file.getSize() == 0 || file.getContentType().isEmpty()) {
            throw new IllegalArgumentException("Can't save an empty file");
        }
        if(!file.getContentType().startsWith("image/")) {
            throw new IllegalArgumentException("Can't save an image of type " + file.getContentType());
        }


        try {
            String name = security.bytesToHash(file.getBytes());

            var fileName = name + '.' + FilenameUtils.getExtension(StringUtils.cleanPath(file.getOriginalFilename()));

            Path path = Paths.get(uploadDir, fileName);

            if(Files.exists(path)) return accessSrc + fileName;

            Files.copy(file.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            return accessSrc + fileName;
        } catch (NoSuchAlgorithmException e) {
            throw new ServiceUnavailableException("Can't save an image");
        }
    }
}
