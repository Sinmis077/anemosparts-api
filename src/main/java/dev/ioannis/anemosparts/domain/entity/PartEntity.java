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
    private Long partId;
    private String partName;
    private String partDescription;
    private Double partPrice;
    private String partIsbn;
    private String partNumber;
    private Integer partStock;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "part_model",
            joinColumns = @JoinColumn(name = "partId"),
            inverseJoinColumns = {
                    @JoinColumn(name = "modelName", referencedColumnName = "modelName"),
                    @JoinColumn(name = "modelProductionYear", referencedColumnName = "modelProductionYear")
            }
    )
    private Set<ModelEntity> partModels;
}
