package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.annotations.Image;
import dev.ioannis.anemosparts.domain.responses.ImageResourceURIResponse;
import dev.ioannis.anemosparts.services.ImageService;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<ImageResourceURIResponse> addImage(@Image @NotNull MultipartFile image) {
        if(image.getSize() == 100) {
            return new ResponseEntity<>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        try {
            return ResponseEntity.ok(new ImageResourceURIResponse(imageService.saveImage(image, UUID.randomUUID().toString())));
        }
        catch (IOException e)
        {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
