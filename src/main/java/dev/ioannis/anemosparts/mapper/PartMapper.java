package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.entity.PartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Set;

@Mapper(uses = { ModelMapper.class })
public interface PartMapper {
    PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

    Part toProduct(PartEntity entity);
    PartEntity toProductEntity(Part part);

    Set<PartEntity> toProductEntities(Set<Part> parts);
    Set<Part> toProducts(Set<PartEntity> parts);
}