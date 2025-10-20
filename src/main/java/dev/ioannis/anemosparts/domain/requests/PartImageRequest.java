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
public class PartImageRequest {
    @NotNull
    private Long id = 0L;

    @NotNull
    private String source;

    @NotNull
    private Boolean isMain = false;

    private Long partId = 0L;
}
