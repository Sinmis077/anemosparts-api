package dev.ioannis.anemosparts.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ModelEntityId {
    private String modelName;
    private Long modelProductionYear;
}
