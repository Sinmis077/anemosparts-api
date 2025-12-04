package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.PartSummaryDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.OemNumber;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.entities.PartImage;
import dev.ioannis.anemosparts.repositories.OemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PartMapper {

    private ModelMapper modelMapper;
    private PartImageMapper imageMapper;
    private OemRepo oemRepo;

    public PartDto toDto(Part entity) {
        var partDto = new PartDto();
        partDto.setId(entity.getId());
        partDto.setName(entity.getName());
        partDto.setDescription(entity.getDescription());
        partDto.setPartNumber(entity.getPartNumber());
        partDto.setPrice(entity.getPrice());
        partDto.setQuantity(entity.getQuantity());

        if(entity.getOemNumber() != null) {
            partDto.setOemNumber(entity.getOemNumber().getNumber());
        }
        else {
            partDto.setOemNumber(null);
        }

        partDto.setImages(imageMapper.toDtos(entity.getImages()));

        partDto.setModels(modelMapper.toDtos(entity.getModels()));

        return partDto;
    }

    public Part toEntity(PartDto dto) {
        var part = new Part();
        part.setId(dto.getId());
        part.setName(dto.getName());
        part.setDescription(dto.getDescription());
        part.setPartNumber(dto.getPartNumber());
        part.setPrice(dto.getPrice());
        part.setQuantity(dto.getQuantity());

        if(dto.getOemNumber() != null) {
            part.setOemNumber(oemRepo.findByNumber(dto.getOemNumber()));
        } else
        {
            part.setOemNumber(null);
        }

        part.setImages(imageMapper.toEntities(dto.getImages()));

        part.setModels(modelMapper.toEntities(dto.getModels()));

        return part;
    }

    public List<PartDto> toDtos(List<Part> entities) {
        return  entities.stream().map(this::toDto).toList();
    }

    public List<Part> toEntities(List<PartDto> dtos) {
        return dtos.stream().map(this::toEntity).toList();
    }

    public List<PartSummaryDto> toSummaries(List<Part> parts) {
        var summaries = new ArrayList<PartSummaryDto>();

        for (Part part : parts) {
            PartImage thumbnail = null;
            if(!part.getImages().isEmpty()) {
                try{
                    if(part.getImages().stream().anyMatch(PartImage::getIsThumbnail)) {
                        //noinspection OptionalGetWithoutIsPresent
                        thumbnail = part.getImages().stream().filter(PartImage::getIsThumbnail).findFirst().get();
                    }
                } catch (NullPointerException ignored) {}
            }

            summaries.add(new PartSummaryDto(
                    part.getId(),
                    part.getName(),
                    part.getDescription(),
                    part.getOemNumber() != null ? part.getOemNumber().getNumber() : null,
                    part.getPartNumber(),
                    part.getPrice(),
                    part.getQuantity(),
                    Optional.ofNullable(thumbnail != null ? thumbnail.getSource() : ""),
                    Optional.ofNullable(part.getModels().getFirst().getBrand().getIconUrl()),
                    part.getModels().stream().map(Model::getId).toList()
            ));
        }

        return summaries;
    }

    public Part toEntity(PartSaveRequest request) {
        var part = new Part();
        part.setName(request.getName());
        part.setPartNumber(request.getPartNumber());

        // OEM number can be null this is not a mistake.
        if(request.getOemNumber() != null) {
            if (!request.getOemNumber().isBlank()) {
                var oemNumber = new OemNumber();
                oemNumber.setNumber(request.getOemNumber());
                part.setOemNumber(oemNumber);
            }
        }
        else part.setOemNumber(null);

        part.setPrice(request.getPrice());
        part.setQuantity(request.getQuantity());
        part.setDescription(request.getDescription());

        part.setModels(request.getModelIds().stream().map(modelId -> Model.builder().id(modelId).build()).collect(Collectors.toList()));
        part.setImages(imageMapper.toEntitiesFromRequest(request.getImages()));

        return part;
    }
}