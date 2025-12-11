package dev.ioannis.anemosparts.daemons;

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
public class CartItem {
    @NotNull
    @Positive
    private long partId;
    @NotNull
    @Positive
    private int quantity;
}
