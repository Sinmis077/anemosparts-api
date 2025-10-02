package dev.ioannis.anemosparts.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "model")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ModelEntity {
    @EmbeddedId
    private ModelEntityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand", referencedColumnName = "name")
    private BrandEntity brand;
}
