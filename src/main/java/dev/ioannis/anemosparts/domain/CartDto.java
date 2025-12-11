package dev.ioannis.anemosparts.domain;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDto {
    @Size(min = 1)
    @NotEmpty
    @Builder.Default
    private List<PartTransactionDto> parts = new ArrayList<>();
}
