package dev.ioannis.anemosparts.services;

import java.io.IOException;
import java.io.InputStream;

public interface ImageService {
    String save(InputStream imageStream, String imageOriginalName, String imageContent, byte[] imageBytes) throws IOException;
}
