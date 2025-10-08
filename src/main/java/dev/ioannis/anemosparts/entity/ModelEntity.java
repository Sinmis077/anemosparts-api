package dev.ioannis.anemosparts.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "model")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ModelEntity {
    @EmbeddedId
    private ModelEntityId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand", referencedColumnName = "name")
    private BrandEntity brand;
}
