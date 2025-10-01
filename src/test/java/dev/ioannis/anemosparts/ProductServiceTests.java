package dev.ioannis.anemosparts;

import dev.ioannis.anemosparts.business.impl.PartServiceImpl;
import dev.ioannis.anemosparts.persistance.PartRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
}
