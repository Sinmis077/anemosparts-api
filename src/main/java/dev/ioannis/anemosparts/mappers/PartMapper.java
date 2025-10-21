package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

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
}