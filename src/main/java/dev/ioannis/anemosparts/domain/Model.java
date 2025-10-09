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
public class Model {
    private String name;
    private int productionYear;
    private Brand brand;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;

        return Objects.equals(name, model.name) && Objects.equals(productionYear, model.productionYear) && Objects.equals(brand, model.brand);
    }
}
