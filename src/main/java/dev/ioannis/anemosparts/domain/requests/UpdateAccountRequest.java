package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record UpdateAccountRequest(@NotNull @Size(min = 2) String forename, @NotNull @Size(min = 2) String surname) {
}
