package dev.ioannis.anemosparts.domain.requests;

import dev.ioannis.anemosparts.enums.OrderStatus;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateOrderStatusRequest {
    @NotNull
    @Positive
    private Long orderId;
    @NotNull
    private OrderStatus status;
}
