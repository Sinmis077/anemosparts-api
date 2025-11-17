package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.annotations.Image;
import dev.ioannis.anemosparts.domain.responses.ImageResourceURLResponse;
import dev.ioannis.anemosparts.services.ImageService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<ImageResourceURLResponse> addImage(@Image @NotNull MultipartFile image) throws IOException {
        if(image.getSize() == 100) {
            throw new HttpClientErrorException(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        var imageUrl = imageService.save(image.getInputStream(),
                                         image.getOriginalFilename(),
                                         image.getContentType(),
                                         image.getBytes());

        return ResponseEntity.ok(new ImageResourceURLResponse(imageUrl));
    }
}
