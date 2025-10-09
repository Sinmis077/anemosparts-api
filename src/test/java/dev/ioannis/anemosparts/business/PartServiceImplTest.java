package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.business.impl.PartServiceImpl;
import dev.ioannis.anemosparts.domain.Brand;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.entity.BrandEntity;
import dev.ioannis.anemosparts.entity.ModelEntity;
import dev.ioannis.anemosparts.entity.ModelEntityId;
import dev.ioannis.anemosparts.entity.PartEntity;
import dev.ioannis.anemosparts.persistance.PartRepo;
import org.hibernate.service.spi.ServiceException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceImplTest {

    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private PartServiceImpl partService;

    @Test
    void findAll_found_nothing() {
        when(partRepo.findAll()).thenReturn(List.of());

        var result = partService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findAll();
    }

    @Test
    void findAll_found_something() {
        var partEntity = new PartEntity();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");

        when(partRepo.findAll()).thenReturn(List.of(partEntity));

        var result = partService.findAll();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        verify(partRepo, times(1)).findAll();
    }

    @Test
    void findByName_found_nothing() {
        when(partRepo.findByName("Gearbox")).thenReturn(List.of());

        var result = partService.findByName("Gearbox");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findByName("Gearbox");
    }

    @Test
    void findByName_found_something() {
        var entity = new PartEntity();
        entity.setId(1L);
        entity.setName("Gearbox");

        when(partRepo.findByName("Gearbox")).thenReturn(List.of(entity));

        var result = partService.findByName("Gearbox");

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.stream().anyMatch(part -> part.getName().equals("Gearbox")));
        verify(partRepo, times(1)).findByName("Gearbox");
    }

    @Test
    void findByModel_found_nothing() {
        var model = new Model("TZR250", 1991, new Brand(1L, "Yamaha"));
        var modelEntityId = new ModelEntityId("TZR250", 1991);

        when(partRepo.findByModels(any(ModelEntityId.class))).thenReturn(List.of());

        var result = partService.findByModel(model);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findByModels(any(ModelEntityId.class));
    }

    @Test
    void findByModel_found_something() {
        var model = new Model("TZR250", 1991, new Brand(1L, "Yamaha"));
        var modelEntityId = new ModelEntityId("TZR250", 1991);
        var modelEntity = ModelEntity.builder()
                .id(modelEntityId)
                .brand(new BrandEntity(1L, "Yamaha"))
                .build();
        var partEntity = PartEntity.builder()
                .id(1L)
                .name("Gearbox")
                .models(List.of(modelEntity))
                .build();

        when(partRepo.findByModels(any(ModelEntityId.class))).thenReturn(List.of(partEntity));

        var result = partService.findByModel(model);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertTrue(result.stream().allMatch(part -> part.getModels().contains(model)));
        verify(partRepo, times(1)).findByModels(any(ModelEntityId.class));
    }

    @Test
    void findById_found_nothing() {
        when(partRepo.findById(1L)).thenReturn(Optional.empty());

        var result = partService.findById(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findById(1L);
    }

    @Test
    void findById_found_something() {
        var partEntity = new PartEntity();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");

        when(partRepo.findById(1L)).thenReturn(Optional.of(partEntity));

        var result = partService.findById(1L);

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("Gearbox", result.get().getName());
        verify(partRepo, times(1)).findById(1L);
    }

    @Test
    void findByISBN_found_nothing() {
        when(partRepo.findByIsbn("ISBN123")).thenReturn(Optional.empty());

        var result = partService.findByISBN("ISBN123");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findByIsbn("ISBN123");
    }

    @Test
    void findByISBN_found_something() {
        var partEntity = new PartEntity();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");
        partEntity.setIsbn("ISBN123");

        when(partRepo.findByIsbn("ISBN123")).thenReturn(Optional.of(partEntity));

        var result = partService.findByISBN("ISBN123");

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("ISBN123", result.get().getIsbn());
        verify(partRepo, times(1)).findByIsbn("ISBN123");
    }

    @Test
    void findByPartNumber_found_nothing() {
        when(partRepo.findByPartNumber("PN-12345")).thenReturn(Optional.empty());

        var result = partService.findByPartNumber("PN-12345");

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(partRepo, times(1)).findByPartNumber("PN-12345");
    }

    @Test
    void findByPartNumber_found_something() {
        var partEntity = new PartEntity();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");
        partEntity.setPartNumber("PN-12345");

        when(partRepo.findByPartNumber("PN-12345")).thenReturn(Optional.of(partEntity));

        var result = partService.findByPartNumber("PN-12345");

        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        assertEquals("PN-12345", result.get().getPartNumber());
        verify(partRepo, times(1)).findByPartNumber("PN-12345");
    }

    @Test
    void save() {
        var part = new Part();
        part.setName("Gearbox");

        var partEntity = new PartEntity();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");

        when(partRepo.save(any(PartEntity.class))).thenReturn(partEntity);

        var result = partService.save(part);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Gearbox", result.getName());
        verify(partRepo, times(1)).save(any(PartEntity.class));
    }

    @Test
    void deleteById_success() {
        doNothing().when(partRepo).deleteById(1L);

        assertDoesNotThrow(() -> partService.deleteById(1L));

        verify(partRepo, times(1)).deleteById(1L);
    }

    @Test
    void deleteById_throwsException() {
        doThrow(new RuntimeException("Database error")).when(partRepo).deleteById(1L);

        assertThrows(ServiceException.class, () -> partService.deleteById(1L));
        verify(partRepo, times(1)).deleteById(1L);
    }

    @Test
    void delete_success() {
        var part = new Part();
        part.setId(1L);
        part.setName("Gearbox");

        doNothing().when(partRepo).delete(any(PartEntity.class));

        assertDoesNotThrow(() -> partService.delete(part));

        verify(partRepo, times(1)).delete(any(PartEntity.class));
    }

    @Test
    void delete_throwsException() {
        var part = new Part();
        part.setId(1L);
        part.setName("Gearbox");

        doThrow(new RuntimeException("Database error")).when(partRepo).delete(any(PartEntity.class));

        assertThrows(ServiceException.class, () -> partService.delete(part));
        verify(partRepo, times(1)).delete(any(PartEntity.class));
    }
}