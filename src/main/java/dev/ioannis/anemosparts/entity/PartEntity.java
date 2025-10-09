package dev.ioannis.anemosparts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "part")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PartEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 500)
    private String description;
    private Double price;
    private String isbn;
    private String partNumber;
    private Integer quantity;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "part_model",
            joinColumns = @JoinColumn(name = "partId"),
            inverseJoinColumns = {
                    @JoinColumn(name = "modelName", referencedColumnName = "name"),
                    @JoinColumn(name = "modelProductionYear", referencedColumnName = "productionYear")
            }
    )
    private List<ModelEntity> models;
}
