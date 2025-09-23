package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.entity.BrandEntity;
import dev.ioannis.anemosparts.domain.entity.ModelEntityId;
import dev.ioannis.anemosparts.persistance.BrandRepo;
import org.springframework.beans.factory.annotation.Autowired;
import dev.ioannis.anemosparts.persistance.ModelRepo;
import dev.ioannis.anemosparts.domain.entity.ModelEntity;
import dev.ioannis.anemosparts.domain.Model;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {
    @Autowired
    private static BrandRepo brandRepository;

    public static Model convertToModel(ModelEntity entity) {
        return new Model(
                entity.getModelId().getModelName(),
                entity.getModelId().getModelProductionYear(),
                entity.getModelBrand().getBrandName()
        );
    }

    public static ModelEntity convertToEntity(Model model) {
        return new ModelEntity(
                new ModelEntityId(model.getName(), model.getProductionDate()),
                new BrandEntity(model.getBrand())
        );
    }
}
