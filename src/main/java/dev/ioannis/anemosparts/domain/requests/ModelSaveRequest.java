package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelSaveRequest {
    @NotNull
    private String name;
    @NotNull
    @Min(1885)
    @Max(2026)
    private int productionYear;
    @NotNull
    private long brandId;
}
