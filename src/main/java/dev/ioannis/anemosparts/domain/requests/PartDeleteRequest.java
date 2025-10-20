package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PartDeleteRequest {
    @NotNull
    private long id;
}
