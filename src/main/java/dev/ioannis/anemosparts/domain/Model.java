package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Model {
    private String name;
    private Long productionDate;
    private Brand brand;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;
        Model model = (Model) o;

        return Objects.equals(name, model.name) && Objects.equals(productionDate, model.productionDate) && Objects.equals(brand, model.brand);
    }
}
