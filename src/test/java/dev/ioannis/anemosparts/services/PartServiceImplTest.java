package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.mappers.*;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import dev.ioannis.anemosparts.services.impl.PartServiceImpl;
import dev.ioannis.anemosparts.repositories.PartRepo;
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

    // Part mapper uses
    @Mock
    private ModelMapper modelMapper = new ModelMapperImpl();
    @Mock
    private BrandMapper brandMapper = new BrandMapperImpl();
    @Mock
    private PartImageMapper partImageMapper = new PartImageMapperImpl();


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
        var partEntity = new Part();
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
    void save() {
        var part = new PartSaveRequest();
        part.setId(1L);
        part.setName("Gearbox");

        var partEntity = new Part();
        partEntity.setId(1L);
        partEntity.setName("Gearbox");

        when(partRepo.save(any(Part.class))).thenReturn(partEntity);

        var result = partService.save(part);

        assertNotNull(result);
        assertEquals(part.getId(), result.getId());
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