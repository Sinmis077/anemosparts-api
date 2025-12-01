package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.PartSummaryDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.mappers.*;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.repositories.PartRepo;
import dev.ioannis.anemosparts.services.impl.PartServiceImpl;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PartServiceImplTest {

    @Mock
    private PartRepo partRepo;
    @Mock
    private ModelRepo modelRepo;
    @Mock
    private OemRepo oemRepo;
    @Mock
    private PartMapper partMapper;

    @InjectMocks
    private PartServiceImpl partService;

    private PartSaveRequest request;
    private Part part;
    private PartDto partDto;

    @BeforeEach
    void setUp() {
        request = new PartSaveRequest();
        request.setName("Gearbox");
        request.setModelIds(List.of(1L, 2L));

        part = new Part();
        part.setId(1L);
        part.setName("Gearbox");
        part.setModels(List.of(Model.builder().id(1L).build(), Model.builder().id(2L).build()));

        partDto = PartDto.builder()
                .id(1L)
                .name("Gearbox")
                .build();
    }

    @Test
    void findAll_returnsEmptyList_whenNoPartsExist() {
        when(partRepo.findAll()).thenReturn(List.of());

        var result = partService.findAll();

        assertTrue(result.parts().isEmpty());
        verify(partRepo).findAll();
    }

    @Test
    void findAll_returnsParts_whenPartsExist() {
        PartSummaryDto dto = new PartSummaryDto();
        dto.setId(1L);

        when(partRepo.findAll()).thenReturn(List.of(part));
        when(partMapper.toSummaries(List.of(part))).thenReturn(List.of(dto));

        var result = partService.findAll();

        assertEquals(1, result.parts().size());
        verify(partRepo).findAll();
    }

    @Test
    void findAllFull_returnsEmptyList_whenNoPartsExist() {
        when(partRepo.findAll()).thenReturn(List.of());

        var result = partService.findAllFull();

        assertTrue(result.parts().isEmpty());
        verify(partRepo).findAll();
    }

    @Test
    void findAllFull_returnsParts_whenPartsExist() {
        when(partRepo.findAll()).thenReturn(List.of(part));
        when(partMapper.toDtos(List.of(part))).thenReturn(List.of(partDto));

        var result = partService.findAllFull();

        assertEquals(1, result.parts().size());
        verify(partRepo).findAll();
    }

    @Test
    void save_throwsException_whenModelIdsAreNull() {
        request.setModelIds(null);

        assertThrows(IllegalArgumentException.class, () -> partService.save(request));
        verify(partRepo, never()).save(any());
    }

    @Test
    void save_throwsException_whenModelIdsAreEmpty() {
        request.setModelIds(List.of());

        assertThrows(IllegalArgumentException.class, () -> partService.save(request));
        verify(partRepo, never()).save(any());
    }

    @Test
    void save_returnsSavedPart_whenValidRequest() {
        when(partMapper.toEntity(request)).thenReturn(part);
        when(modelRepo.findAllById(List.of(1L, 2L))).thenReturn(List.of(new Model(), new Model()));
        when(partRepo.save(part)).thenReturn(part);
        when(partMapper.toDto(part)).thenReturn(partDto);

        var result = partService.save(request);

        assertEquals("Gearbox", result.getName());
        verify(partRepo).save(any());
    }

    @Test
    void delete_succeeds_whenPartExists() {
        when(partRepo.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> partService.delete(1L));
        verify(partRepo).deleteById(1L);
    }

    @Test
    void delete_throwsException_whenPartDoesNotExist() {
        when(partRepo.existsById(1L)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> partService.delete(1L));
        verify(partRepo, never()).deleteById(any());
    }
}