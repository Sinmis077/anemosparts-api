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
public class PartImageSaveRequest {
    @NotNull
    private Long id;

    @NotNull
    private String source;

    @NotNull
    private Boolean isThumbnail = false;
}
