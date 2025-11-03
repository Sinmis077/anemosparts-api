package dev.ioannis.anemosparts.services.impl;

import ch.qos.logback.core.util.StringUtil;
import dev.ioannis.anemosparts.services.ImageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public class ImageServiceImpl implements ImageService {

    @Override
    public String saveImage(MultipartFile file) {
        if(file.isEmpty() || file.getSize() == 0) {
            throw new IllegalArgumentException("Can't save an empty file");
        }
        if(!file.getContentType().equals("image/")) {
            throw new IllegalArgumentException("Can't save an image of type " + file.getContentType());
        }

        return "";
    }
}
