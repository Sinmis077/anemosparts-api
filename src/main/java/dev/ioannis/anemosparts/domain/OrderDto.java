package dev.ioannis.anemosparts.domain;

import dev.ioannis.anemosparts.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private OrderStatus status = OrderStatus.PAID;
    private String customerEmail;
    private AddressDto address;
    private List<Long> partIds;
    private List<Integer> quantities;
}
