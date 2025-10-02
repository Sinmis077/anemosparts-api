package dev.ioannis.anemosparts;

import dev.ioannis.anemosparts.business.impl.PartServiceImpl;
import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.domain.entity.PartEntity;
import dev.ioannis.anemosparts.persistance.PartRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProductServiceTests {
    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private PartServiceImpl partService;

    @Test
    void testFindAll_emptySet() {
        when(partRepo.findAll()).thenReturn(Set.of());

        var result = partService.findAll();

        assert result != null && result.isEmpty();
    }

    @Test
    void testFindAll_success() {
        var partEntity = new PartEntity();

        when(partRepo.findAll()).thenReturn(Set.of(partEntity));
        var result = partService.findAll();

        assert result != null && !result.isEmpty();
    }

    @Test
    void testFindByName_notFound() {
        when(partRepo.findAll()).thenReturn(Set.of());

        var result = partService.findByName("Gearbox");

        assert result != null && result.isEmpty();
    }

    @Test
    void testFindByName_Found() {
        var entity = new PartEntity();
        entity.setName("Gearbox");

        when(partRepo.findAll()).thenReturn(Set.of(entity));

        var result = partService.findByName("Gearbox");
        assert result != null && result.stream().findFirst().filter(part -> Objects.equals(part.getName(), "Gearbox")).isPresent();
    }
}
