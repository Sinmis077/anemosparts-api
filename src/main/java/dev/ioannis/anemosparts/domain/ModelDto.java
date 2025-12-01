package dev.ioannis.anemosparts.domain;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {
    private Long id;
    @NotBlank
    private String name;
    @NotNull
    @Min(1885)
    @Max(2026)
    private Integer productionYear;
    @NotNull
    private BrandDto brand;

    @Override
    public String toString() {
        return this.brand.getName() + this.name + "(" + this.productionYear + ")";
    }
}
