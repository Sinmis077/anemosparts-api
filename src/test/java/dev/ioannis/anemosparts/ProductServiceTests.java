package dev.ioannis.anemosparts;

import dev.ioannis.anemosparts.business.PartService;
import dev.ioannis.anemosparts.business.impl.PartServiceImpl;
import dev.ioannis.anemosparts.persistance.PartRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class ProductServiceTests {
    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private PartServiceImpl partService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindAll_EmptyList() {
        assert partService.findAll().isEmpty();
    }
}
