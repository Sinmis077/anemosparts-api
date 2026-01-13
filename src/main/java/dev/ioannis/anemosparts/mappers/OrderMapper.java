package dev.ioannis.anemosparts.mappers;

import dev.ioannis.anemosparts.domain.OrderDto;
import dev.ioannis.anemosparts.domain.PartTransactionDto;
import dev.ioannis.anemosparts.entities.Order;
import dev.ioannis.anemosparts.entities.PartTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", uses = {AddressMapper.class})
public interface OrderMapper {
    List<OrderDto> toDtos(List<Order> orders);

    @Mapping(source = "account.email", target = "customerEmail")
    @Mapping(source = "orderStatus", target = "status")
    @Mapping(source = "shippingAddress", target = "address")
    @Mapping(target = "items", source = "partTransactions")
    OrderDto toDto(Order order);

    @Mapping(target = "partId", source = "part.id")
    PartTransactionDto toPartTransactionDto(PartTransaction partTransaction);
}
