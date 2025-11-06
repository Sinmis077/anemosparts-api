package dev.ioannis.anemosparts.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile file, String name) throws IOException;
}
