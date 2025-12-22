package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.entities.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    List<OrderDto> toDtos(List<Order> orders);

    OrderDto toDto(Order order);
}
