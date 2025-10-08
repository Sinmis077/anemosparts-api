package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.business.impl.PartServiceImpl;
import dev.ioannis.anemosparts.domain.Brand;
import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.entity.BrandEntity;
import dev.ioannis.anemosparts.entity.ModelEntity;
import dev.ioannis.anemosparts.entity.ModelEntityId;
import dev.ioannis.anemosparts.entity.PartEntity;
import dev.ioannis.anemosparts.persistance.PartRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;
import java.util.Set;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PartServiceImplTest {

    @Mock
    private PartRepo partRepo;

    @InjectMocks
    private PartServiceImpl partService;

    @Test
    void findAll_found_nothing() {
        when(partRepo.findAll()).thenReturn(Set.of());

        var result = partService.findAll();

        assert result != null && result.isEmpty();
    }

    @Test
    void findAll_found_something() {
        var partEntity = new PartEntity();

        when(partRepo.findAll()).thenReturn(Set.of(partEntity));
        var result = partService.findAll();

        assert result != null && !result.isEmpty();
    }

    @Test
    void findByName_found_nothing() {
        when(partRepo.findAll()).thenReturn(Set.of());

        var result = partService.findByName("Gearbox");

        assert result != null && result.isEmpty();
    }

    @Test
    void findByName_found_something() {
        var entity = new PartEntity();
        entity.setName("Gearbox");

        when(partRepo.findAll()).thenReturn(Set.of(entity));

        var result = partService.findByName("Gearbox");
        assert result != null && result.stream().anyMatch(part -> part.getName().equals("Gearbox"));
    }

    @Test
    void findByModel_found_nothing() {
        var model = new Model();

        when(partRepo.findAll()).thenReturn(Set.of());

        var result = partService.findByModel(model);

        assert result != null && result.isEmpty();
    }

    @Test
    void findByModel_found_something() {
        var model = new Model("TZR250", 1991L, new Brand("Yamaha"));
        var modelEnttiy  = ModelEntity.builder()
                .id(new ModelEntityId(model.getName(), model.getProductionDate()))
                .brand(new BrandEntity(model.getBrand().getName()))
                .build();
        var partEntity = PartEntity.builder().models(Set.of(modelEnttiy)).build();

        when(partRepo.findAll()).thenReturn(Set.of(partEntity));

        var result = partService.findByModel(model);

        assert result != null && result.stream().allMatch(part ->  part.getModels().contains(model));
    }

    @Test
    void findById() {
    }

    @Test
    void findByISBN() {
    }

    @Test
    void findByPartNumber() {
    }

    @Test
    void save() {
    }

    @Test
    void deleteById() {
    }

    @Test
    void delete() {
    }
}