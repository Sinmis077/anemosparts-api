package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandSaveRequest {
    @NotNull
    private String name;
    private String iconUrl;
}
