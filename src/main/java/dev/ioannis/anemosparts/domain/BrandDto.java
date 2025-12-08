package dev.ioannis.anemosparts.domain;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BrandDto {
    private Long id;

    @NotNull
    private String name;

    private String iconUrl;

    @Override
    public String toString() {
        return this.name;
    }
}
