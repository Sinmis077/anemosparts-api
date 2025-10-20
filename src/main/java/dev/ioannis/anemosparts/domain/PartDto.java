package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;

import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PartDto {
    private long id;
    private String name;
    private String description;
    private String oemNumber;
    private String partNumber;
    private double price;
    private int quantity;

    private List<ModelDto> models;

    private List<PartImageDto> images;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PartDto part = (PartDto) o;

        if (id != part.id) return false;
        if (!Objects.equals(name, part.name)) return false;
        if (!Objects.equals(description, part.description)) return false;
        if (!Objects.equals(oemNumber, part.oemNumber)) return false;
        if (!Objects.equals(partNumber, part.partNumber)) return false;
        if (!Objects.equals(price, part.price)) return false;
        if (!Objects.equals(quantity, part.quantity)) return false;
        return Objects.equals(models, part.models);
    }
}
