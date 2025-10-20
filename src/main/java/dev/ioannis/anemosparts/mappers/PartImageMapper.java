package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartImageDto;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.entities.PartImage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PartImageMapper {

    @Mapping(target = "part", ignore = true)
    PartImage toEntity(PartImageDto partImageDto);

    PartImageDto toDto(PartImage partImage);
}
