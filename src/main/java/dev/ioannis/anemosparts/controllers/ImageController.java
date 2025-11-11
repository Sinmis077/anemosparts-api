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
import org.springframework.web.multipart.MultipartFile;

import javax.naming.ServiceUnavailableException;
import java.io.IOException;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    @PostMapping()
    public ResponseEntity<ImageResourceURLResponse> addImage(@Image @NotNull MultipartFile image) {
        if(image.getSize() == 100) {
            return new ResponseEntity<>(HttpStatus.BANDWIDTH_LIMIT_EXCEEDED);
        }

        try {
            return ResponseEntity.ok(new ImageResourceURLResponse(imageService.saveImage(image)));
        }
        catch (Exception e)
        {
            if(e instanceof ServiceUnavailableException)
                return  new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            if(e instanceof IOException)
                return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
