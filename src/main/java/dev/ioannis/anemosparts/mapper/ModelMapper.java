package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.entity.BrandEntity;
import dev.ioannis.anemosparts.domain.entity.ModelEntityId;
import dev.ioannis.anemosparts.persistance.BrandRepo;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import dev.ioannis.anemosparts.persistance.ModelRepo;
import dev.ioannis.anemosparts.domain.entity.ModelEntity;
import dev.ioannis.anemosparts.domain.Model;
import org.springframework.stereotype.Component;

@Mapper
public interface ModelMapper {

    ModelMapper INSTANCE = Mappers.getMapper(ModelMapper.class);

    public Model toModel(ModelEntity modelEntity);
    public ModelEntity toModelEntity (Model model);
}
