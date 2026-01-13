package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record UpdateAccountRequest(@NotNull @Size(min = 6) String forename, @NotNull @Size(min = 6) String surname) {
}
