package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModelDto {
    private long id;
    private String name;
    private int productionYear;
    private BrandDto brand;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        ModelDto modelDto = (ModelDto) o;

        return Objects.equals(name, modelDto.name) && Objects.equals(productionYear, modelDto.productionYear) && Objects.equals(brand, modelDto.brand);
    }
}
