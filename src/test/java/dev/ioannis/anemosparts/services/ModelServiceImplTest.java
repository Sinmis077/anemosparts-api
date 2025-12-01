package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.domain.requests.ModelSaveRequest;
import dev.ioannis.anemosparts.entities.Brand;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.exceptions.NullBrandException;
import dev.ioannis.anemosparts.mappers.ModelMapper;
import dev.ioannis.anemosparts.repositories.BrandRepo;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.services.impl.ModelServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
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
class ModelServiceImplTest {

    @Mock
    private ModelRepo modelRepo;
    @Mock
    private BrandRepo brandRepo;
    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private ModelServiceImpl modelService;

    private ModelSaveRequest request;
    private Model model;
    private ModelDto modelDto;
    private Brand brand;

    @BeforeEach
    void setUp() {
        brand = new Brand();
        brand.setId(1L);
        brand.setName("Kawasaki");

        request = new ModelSaveRequest();
        request.setName("ZX-10R");
        request.setProductionYear(2020);
        request.setBrandId(1L);

        model = new Model();
        model.setId(1L);
        model.setName("ZX-10R");
        model.setProductionYear(2020);
        model.setBrand(brand);

        modelDto = new ModelDto();
        modelDto.setId(1L);
        modelDto.setName("ZX-10R");
        modelDto.setProductionYear(2020);
    }

    @Test
    void findAll_returnsEmptyList_whenNoModelsExist() {
        when(modelRepo.findAll()).thenReturn(List.of());

        var result = modelService.findAll();

        assertTrue(result.models().isEmpty());
        verify(modelRepo).findAll();
    }

    @Test
    void findAll_returnsModels_whenModelsExist() {
        when(modelRepo.findAll()).thenReturn(List.of(model));
        when(modelMapper.toDtos(List.of(model))).thenReturn(List.of(modelDto));

        var result = modelService.findAll();

        assertEquals(1, result.models().size());
        verify(modelRepo).findAll();
    }

    @Test
    void save_returnsSavedModel() {
        when(brandRepo.findById(any(Long.class))).thenReturn(Optional.of(brand));
        when(modelRepo.save(any(Model.class))).thenReturn(model);
        when(modelMapper.toDto(model)).thenReturn(modelDto);

        var result = modelService.save(request);

        assertEquals("ZX-10R", result.getName());
        verify(modelRepo).save(any(Model.class));
    }

    @Test
    void update_returnsUpdatedModel_whenBrandExists() {
        when(brandRepo.findById(1L)).thenReturn(Optional.of(brand));
        when(modelRepo.save(any(Model.class))).thenReturn(model);
        when(modelMapper.toDto(model)).thenReturn(modelDto);

        var result = modelService.update(1L, request);

        assertEquals("ZX-10R", result.getName());
        verify(modelRepo).save(any(Model.class));
    }

    @Test
    void update_throwsException_whenBrandDoesNotExist() {
        when(brandRepo.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> modelService.update(1L, request));
        verify(modelRepo, never()).save(any());
    }

    @Test
    void delete_succeeds_whenModelExists() {
        when(modelRepo.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> modelService.delete(1L));
        verify(modelRepo).deleteById(1L);
    }

    @Test
    void delete_throwsException_whenModelDoesNotExist() {
        when(modelRepo.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> modelService.delete(1L));
        verify(modelRepo, never()).deleteById(any());
    }
}