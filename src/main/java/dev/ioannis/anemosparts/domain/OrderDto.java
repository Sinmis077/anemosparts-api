package dev.ioannis.anemosparts.domain;

import dev.ioannis.anemosparts.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    @Builder.Default
    private OrderStatus status = OrderStatus.PAID;
    private LocalDateTime orderDate;
    private LocalDateTime lastUpdate;
    private BigDecimal total;
    private String customerEmail;
    private AddressDto address;
    private List<PartTransactionDto> items;
}