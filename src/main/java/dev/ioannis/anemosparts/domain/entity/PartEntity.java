package dev.ioannis.anemosparts.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "part")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String isbn;
    private String number;
    private Integer stock;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "part_model",
            joinColumns = @JoinColumn(name = "partId"),
            inverseJoinColumns = {
                    @JoinColumn(name = "modelName", referencedColumnName = "name"),
                    @JoinColumn(name = "modelProductionYear", referencedColumnName = "productionYear")
            }
    )
    private Set<ModelEntity> models;
}
