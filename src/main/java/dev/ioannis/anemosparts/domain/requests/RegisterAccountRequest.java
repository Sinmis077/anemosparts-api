package dev.ioannis.anemosparts.domain.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterAccountRequest(
        @NotNull
        @Size(min = 6)
        String forename,

        @NotNull
        @Size(min = 6)
        String surname,

        @Email
        @NotNull
        String email,

        @NotNull
        @Size(min = 8, max = 70, message = "Password must be between 8 and 70 characters")
        String password
) {
}
