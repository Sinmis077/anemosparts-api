package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.BrandDto;
import dev.ioannis.anemosparts.entities.Brand;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface BrandMapper {

    BrandDto toDto(Brand entity);

    Brand toEntity(BrandDto model);

    List<BrandDto> toDtos(List<Brand> entities);

    List<Brand> toEntities(List<BrandDto> models);
}
