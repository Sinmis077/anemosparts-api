package dev.ioannis.anemosparts.domain;

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
public class PartTransactionDto {
    @NotNull
    @Positive
    private Long partId;
    @NotNull
    @Positive
    private Long quantity;
}
