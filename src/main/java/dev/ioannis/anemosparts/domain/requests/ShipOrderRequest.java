package dev.ioannis.anemosparts.domain.requests;

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
public class ShipOrderRequest {
    @NotNull
    @Positive
    Long orderId;
    @NotNull
    String trackingCode;
}
