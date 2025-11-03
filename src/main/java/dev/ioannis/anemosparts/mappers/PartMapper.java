package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.PartSummaryDto;
import dev.ioannis.anemosparts.entities.Model;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.entities.PartImage;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PartMapper {

    private ModelRepo modelRepo;
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
        partDto.setOemNumber(entity.getOemNumber().getNumber());

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

        part.setOemNumber(oemRepo.findByNumber(dto.getOemNumber()));

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
            var thumbnail = part.getImages().stream().filter(PartImage::getThumbnail).findFirst();

            summaries.add(new PartSummaryDto(
                    part.getId(),
                    part.getName(),
                    part.getDescription(),
                    part.getOemNumber().getNumber(),
                    part.getPartNumber(),
                    part.getPrice(),
                    thumbnail.map(PartImage::getSource),

                    part.getModels().stream().map(Model::getId).toList()
            ));
        }

        return summaries;
    }
}