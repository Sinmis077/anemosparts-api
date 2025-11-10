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

    @Test
    void findAll_found_nothing() {
        when(partRepo.findAll()).thenReturn(List.of());

        var result = partService.findAll();

        assertNotNull(result);
        assertTrue(result.parts().isEmpty());
        verify(partRepo, times(1)).findAll();
    }

    @Test
    void findAll_found_something() {
        var partEntity = new Part();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");

        var partDto = new PartSummaryDto();
        partDto.setId(partEntity.getId());
        partDto.setName(partEntity.getName());

        when(partRepo.findAll()).thenReturn(List.of(partEntity));
        when(partMapper.toSummaries(any(List.class))).thenReturn(List.of(partDto));

        var result = partService.findAll();

        assertNotNull(result);
        assertFalse(result.parts().isEmpty());
        assertEquals(1, result.parts().size());
        verify(partRepo, times(1)).findAll();
    }

    @Test
    void save_missing_modelIds() {
        var part = new PartSaveRequest();
        part.setName("Gearbox");

        assertThrows(IllegalArgumentException.class, () -> partService.save(part));

        verify(partRepo, times(0)).save(any(Part.class));
    }

    @Test
    void save() {
        var part = new PartSaveRequest();
        part.setName("Gearbox");
        part.setModelIds(List.of(1L, 2L));

        var partEntity = new Part();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");
        partEntity.setModels(List.of(Model.builder().id(1L).build(), Model.builder().id(2L).build()));

        when(partRepo.save(any(Part.class))).thenReturn(partEntity);
        when(partMapper.toEntity(any(PartSaveRequest.class))).thenReturn(partEntity);
        when(modelRepo.findAllById(any(List.class))).thenReturn(List.of(new Model(), new Model()));
        when(partMapper.toDto(any(Part.class))).thenReturn(PartDto.builder().id(partEntity.getId()).name(partEntity.getName()).build());

        var result = partService.save(part);

        assertNotNull(result);
        assertEquals(part.getName(), result.getName());
        verify(partRepo, times(1)).save(any(Part.class));
    }

    @Test
    void delete_success() {
        var part = new PartDto();
        part.setId(1L);
        part.setName("Gearbox");

        when(partRepo.existsById(any(Long.class))).thenReturn(true);
        doNothing().when(partRepo).deleteById(any(Long.class));

        assertDoesNotThrow(() -> partService.delete(part.getId()));

        verify(partRepo, times(1)).deleteById(any(Long.class));
    }

    @Test
    void delete_throwsException() {
        var part = new PartDto();
        part.setId(1L);
        part.setName("Gearbox");

        when(partRepo.existsById(any(Long.class))).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> partService.delete(part.getId()));
        verify(partRepo, times(0)).deleteById(any(Long.class));
    }
}