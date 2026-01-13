package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.AddressDto;
import dev.ioannis.anemosparts.entities.Address;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface AddressMapper {

    AddressDto toDto(Address address);
}
