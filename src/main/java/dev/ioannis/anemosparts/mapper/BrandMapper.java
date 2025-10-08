package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Brand;
import dev.ioannis.anemosparts.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    public Brand toBrand(BrandEntity brandEntity);
    public BrandEntity toBrandEntity(Brand brand);
}
