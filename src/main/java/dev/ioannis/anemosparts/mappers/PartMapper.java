package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.Part;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;

@Mapper(componentModel = "spring", uses = {ModelMapper.class})
public interface PartMapper {
    @Mapping(target = "oemNumber", ignore = true)
    PartDto toDto(Part entity);

    Part toEntity(PartDto part);

    @Mapping(target = "oemNumber", ignore = true)
    List<PartDto> toDtos(List<Part> parts);

    List<Part> toEntities(List<PartDto> parts);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Part toEntity(PartSaveRequest request);
}