package dev.ioannis.anemosparts.repositories.impl;

import dev.ioannis.anemosparts.repositories.ImageRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Repository
public class ImageRepoLocalImpl implements ImageRepo {

    private final Path uploadDir;

    public ImageRepoLocalImpl(@Value("${app.upload.dir}") String uploadDir) {
        this.uploadDir = Path.of(uploadDir);

        initializeUploadDir();
    }

    private void initializeUploadDir() {
        try{
            Files.createDirectories(uploadDir);
        }
        catch (IOException e){
            System.out.println("Could not create directory: " + uploadDir);
            System.out.println(e.getMessage());
        }
    }

    @Override
    public String save(InputStream imageStream, String name) throws IOException {
        Path path = uploadDir.resolve(name);

        if(Files.exists(path)) return name;

        Files.copy(imageStream, path, StandardCopyOption.REPLACE_EXISTING);

        return name;
    }

    @Override
    public Boolean exists(String name) {
        return Files.exists(uploadDir.resolve(name));
    }
}
