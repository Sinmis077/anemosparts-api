package dev.ioannis.anemosparts.domain.responses;

import dev.ioannis.anemosparts.domain.OrderDto;

import java.util.List;

public record FindOrdersResponse(List<OrderDto> orders) {
}
