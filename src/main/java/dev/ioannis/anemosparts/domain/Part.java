package dev.ioannis.anemosparts.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

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

    private Set<Model> models;
}
