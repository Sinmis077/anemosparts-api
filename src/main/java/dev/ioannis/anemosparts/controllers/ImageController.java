package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.annotations.Image;
import dev.ioannis.anemosparts.annotations.Images;
import dev.ioannis.anemosparts.domain.responses.ImageResourceURLResponse;
import dev.ioannis.anemosparts.domain.responses.ImageResourceURLSResponse;
import dev.ioannis.anemosparts.services.ImageService;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @Value("${app.image.max.size}")
    private Long maxSize;

    @PostMapping()
    public ResponseEntity<ImageResourceURLResponse> addImage(@Image @NotNull MultipartFile image) throws IOException {
        if(image.getSize() > maxSize) {
            throw new HttpClientErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        var imageUrl = imageService.save(image.getInputStream(),
                                         image.getOriginalFilename(),
                                         image.getContentType(),
                                         image.getBytes());

        return ResponseEntity.ok(new ImageResourceURLResponse(imageUrl));
    }

    @PostMapping("/batch")
    public ResponseEntity<ImageResourceURLSResponse> addImages(@Images @NotNull List<MultipartFile> images) throws IOException {
        if(images.isEmpty()) throw new HttpClientErrorException(HttpStatus.NOT_ACCEPTABLE);
        if(images.size() > 10) throw new HttpClientErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);

        // BAD HABIT: Variable name should be camelCase (imageUrls), not PascalCase (ImageUrls)
        List<String> ImageUrls = new ArrayList<>();
        for(var image : images) {
            if(image.getSize() > maxSize) {
                throw new HttpClientErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
            }

            var imageUrl = imageService.save(image.getInputStream(),
                    image.getOriginalFilename(),
                    image.getContentType(),
                    image.getBytes());

            ImageUrls.add(imageUrl);
        }

        return ResponseEntity.ok(new ImageResourceURLSResponse(ImageUrls));
    }
}
