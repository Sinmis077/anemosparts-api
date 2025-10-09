package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Brand;
import dev.ioannis.anemosparts.entity.BrandEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface BrandMapper {
    BrandMapper INSTANCE = Mappers.getMapper(BrandMapper.class);

    Brand toModel(BrandEntity entity);
    BrandEntity toEntity(Brand model);

    List<Brand> toModels(List<BrandEntity> entities);
    List<BrandEntity> toEntities(List<Brand> models);
}
