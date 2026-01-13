package dev.ioannis.anemosparts.services;

import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateAccountRequest;
import dev.ioannis.anemosparts.entities.Account;

import java.util.Optional;

public interface AccountService {
    Account createAccount(RegisterAccountRequest req);

    Account createAdminAccount(RegisterAccountRequest req);

    Account createGuestAccount(Account account);

    Account updateAccount(String email, UpdateAccountRequest req);

    Optional<Account> findByEmail(String email);

    /**
     * This method does not discriminate on account type it simply checks if it exists.
     * So if an account has a password or not does not matter
     *
     * @param email String that represents the account
     * @return True or false
     */
    Boolean existsByEmail(String email);
}
