package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Part;
import dev.ioannis.anemosparts.domain.entity.PartEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PartMapper {
    PartMapper INSTANCE = Mappers.getMapper(PartMapper.class);

    public Part toProduct(PartEntity entity);
    public PartEntity toProductEntity (Part product);
}