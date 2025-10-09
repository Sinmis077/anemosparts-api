package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Objects;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Part {
    private long id;
    private String name;
    private String description;
    private String isbn;
    private String partNumber;
    private double price;
    private int quantity;

    private List<Model> models;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Part part = (Part) o;

        if(id != part.id) return false;
        if(!Objects.equals(name, part.name)) return false;
        if(!Objects.equals(description, part.description)) return false;
        if(!Objects.equals(isbn, part.isbn)) return false;
        if(!Objects.equals(partNumber, part.partNumber)) return false;
        if(!Objects.equals(price, part.price)) return false;
        if(!Objects.equals(quantity, part.quantity)) return false;
        return Objects.equals(models, part.models);
    }
}
