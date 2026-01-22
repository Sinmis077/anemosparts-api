package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.repositories.ImageRepo;
import dev.ioannis.anemosparts.services.impl.ImageServiceLocalImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceLocalImplTest {

    @Mock
    private SecurityService securityService;
    @Mock
    private ImageRepo imageRepo;

    private ImageServiceLocalImpl imageService;

    private byte[] imageBytes;
    private String imageContent;
    private String imageName;

    @BeforeEach
    void setUp() {
        imageService = new ImageServiceLocalImpl(securityService, imageRepo, "http://localhost:8080/resources/images/");

        imageBytes = new byte[]{1, 2, 3, 4, 5};
        imageContent = "image/png";
        imageName = "test-image.png";
    }

    @Test
    void save_succeeds_whenValidImage() throws Exception {
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        when(securityService.bytesToHash(imageBytes)).thenReturn("hash123");
        when(imageRepo.exists(anyString())).thenReturn(false);
        when(imageRepo.save(any(InputStream.class), anyString())).thenReturn("hash123.png");

        var result = imageService.save(imageStream, imageName, imageContent, imageBytes);

        assertNotNull(result);
        assertTrue(result.contains("hash123.png"));
        verify(imageRepo).save(any(InputStream.class), anyString());
    }

    @Test
    void save_returnsExistingUrl_whenImageAlreadyExists() throws Exception {
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        when(securityService.bytesToHash(imageBytes)).thenReturn("hash123");
        when(imageRepo.exists(anyString())).thenReturn(true);

        var result = imageService.save(imageStream, imageName, imageContent, imageBytes);

        assertNotNull(result);
        assertTrue(result.contains("hash123.png"));
        verify(imageRepo, never()).save(any(InputStream.class), anyString());
    }

    @Test
    void save_throwsException_whenEmptyFile() {
        byte[] emptyBytes = new byte[0];
        InputStream emptyStream = new ByteArrayInputStream(emptyBytes);

        assertThrows(IllegalArgumentException.class,
                () -> imageService.save(emptyStream, imageName, imageContent, emptyBytes));
    }

    @Test
    void save_throwsException_whenEmptyContentType() {
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        assertThrows(IllegalArgumentException.class,
                () -> imageService.save(imageStream, imageName, "", imageBytes));
    }

    @Test
    void save_throwsException_whenInvalidContentType() {
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        assertThrows(IllegalArgumentException.class,
                () -> imageService.save(imageStream, imageName, "text/plain", imageBytes));
    }

    @Test
    void save_throwsIOException_whenHashingFails() throws Exception {
        InputStream imageStream = new ByteArrayInputStream(imageBytes);

        when(securityService.bytesToHash(imageBytes)).thenThrow(new java.security.NoSuchAlgorithmException("Test"));

        assertThrows(IOException.class,
                () -> imageService.save(imageStream, imageName, imageContent, imageBytes));
        verify(imageRepo, never()).save(any(InputStream.class), anyString());
    }
}