package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.entities.Account;

public interface JwtService {
    String generateToken(Account account);

    Boolean isTokenValid(String token);

    String extractEmailFromToken(String token);
}
