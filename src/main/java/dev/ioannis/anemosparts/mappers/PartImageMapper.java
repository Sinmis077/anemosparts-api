package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartImageDto;
import dev.ioannis.anemosparts.domain.requests.PartImageSaveRequest;
import dev.ioannis.anemosparts.entities.PartImage;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PartImageMapper {

    PartImage toEntity(PartImageDto imageDto);

    PartImageDto toDto(PartImage image);

    List<PartImageDto> toDtos(List<PartImage> images);

    List<PartImage> toEntities(List<PartImageDto> images);

    List<PartImage> toEntitiesFromRequest(List<PartImageSaveRequest> image);
}
