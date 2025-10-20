package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.PartDto;
import dev.ioannis.anemosparts.domain.requests.PartSaveRequest;
import dev.ioannis.anemosparts.entities.OemNumber;
import dev.ioannis.anemosparts.entities.Part;
import dev.ioannis.anemosparts.repositories.ModelRepo;
import dev.ioannis.anemosparts.repositories.OemRepo;
import lombok.AllArgsConstructor;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@AllArgsConstructor
public class PartMapper {

    private ModelRepo modelRepo;
    private ModelMapper modelMapper;
    private PartImageMapper partImageMapper;
    private OemRepo oemRepo;

    public PartDto toDto(Part entity) {
        var  partDto = new PartDto();
        partDto.setId(entity.getId());
        partDto.setName(entity.getName());
        partDto.setDescription(entity.getDescription());
        partDto.setPartNumber(entity.getPartNumber());
        partDto.setPrice(entity.getPrice());
        partDto.setQuantity(entity.getQuantity());
        partDto.setOemNumber(entity.getOemNumber().getNumber());

//        partDto.setImages(partImageMapper.toDto(entity.));

        var models = modelMapper.toDtos(entity.getModels());


        if(entity.getOemNumber() != null && oemRepo.existsByNumber(entity.getOemNumber().getNumber())) {
        }

    }

    @Mapping(target = "oemNumber", source = "oemNumber", qualifiedByName = "oemNumberToString")
    PartDto toDto(Part entity);

    @Mapping(target = "oemNumber", ignore = true)
    Part toEntity(PartDto part);

    @Mapping(target = "oemNumber", source = "oemNumber", qualifiedByName = "oemNumberToString")
    List<PartDto> toDtos(List<Part> parts);

    List<Part> toEntities(List<PartDto> parts);

    @Named("oemNumberToString")
    default String oemToString(OemNumber entity) {
        return entity != null ? entity.getNumber() : null;
    }
}