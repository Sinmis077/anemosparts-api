package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.entities.Account;

import java.util.Optional;

public interface AccountService {
    Account createAccount(Account account);

    Optional<Account> findByEmail(String email);

    Boolean existsByEmail(String email);

    //    Jwt getJwt();
}
