package dev.ioannis.anemosparts.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import dev.ioannis.anemosparts.entity.ModelEntity;
import dev.ioannis.anemosparts.domain.Model;

@Mapper(uses = { BrandMapper.class })
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    Model toModel(ModelEntity modelEntity);
    ModelEntity toModelEntity(Model model);
}
