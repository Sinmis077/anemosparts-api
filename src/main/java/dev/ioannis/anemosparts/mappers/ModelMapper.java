package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.ModelDto;
import dev.ioannis.anemosparts.entities.Model;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = {BrandMapper.class})
public interface ModelMapper {
    ModelDto toDto(Model entity);

    Model toEntity(ModelDto modelDto);

    List<ModelDto> toDtos(List<Model> entities);

    List<Model> toEntities(List<ModelDto> dtos);

}
