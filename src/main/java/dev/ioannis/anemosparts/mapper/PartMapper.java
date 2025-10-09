package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.domain.request.NewPartRequest;
import dev.ioannis.anemosparts.entity.PartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = { ModelMapper.class })
public interface PartMapper {
    PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

    Part toModel(PartEntity entity);
    PartEntity toEntity(Part part);

    List<Part> toModels(List<PartEntity> parts);
    List<PartEntity> toEntities(List<Part> parts);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Part toModel(NewPartRequest request);
}