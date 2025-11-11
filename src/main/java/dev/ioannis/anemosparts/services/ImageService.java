package dev.ioannis.anemosparts.services;

import org.springframework.web.multipart.MultipartFile;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;

public interface ImageService {
    String saveImage(MultipartFile file) throws IOException, ServiceUnavailableException;
}
