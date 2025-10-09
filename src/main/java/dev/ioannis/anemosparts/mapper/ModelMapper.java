package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Model;
import dev.ioannis.anemosparts.entity.ModelEntity;
import dev.ioannis.anemosparts.entity.ModelEntityId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = { BrandMapper.class })
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    @Mappings({
        @Mapping(target = "name", source = "id.name"),
        @Mapping(target = "productionYear", source = "id.productionYear")
    })
    Model toModel(ModelEntity entity);
    @Mapping(target = "id", source = ".", qualifiedByName = "toModelEntityId")
    ModelEntity toEntity(Model model);

    List<Model> toModels(List<ModelEntity> entities);
    List<ModelEntity> toEntities(List<Model> models);

    @Named("toModelEntityId")
    default ModelEntityId toModelEntityId(Model model) {
        if (model == null)
            return null;

        return new ModelEntityId(model.getName(),  model.getProductionYear());
    }
}
