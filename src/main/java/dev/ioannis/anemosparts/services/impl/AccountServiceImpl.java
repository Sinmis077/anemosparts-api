package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.domain.requests.UpdateAccountRequest;
import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.enums.UserRole;
import dev.ioannis.anemosparts.repositories.AccountRepo;
import dev.ioannis.anemosparts.services.AccountService;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Account createAccount(RegisterAccountRequest req) {
        var dbAccount = findByEmail(req.email());

        if (dbAccount.isPresent()) {
            var account =  dbAccount.get();

            if(!account.getPassword().isBlank()) throw new EntityExistsException("Account already exists");

            account.setForename(req.forename());
            account.setSurname(req.surname());
            account.setPassword(passwordEncoder.encode(req.password()));

            return accountRepo.save(account);
        }

        var account = Account.builder()
                .forename(req.forename())
                .surname(req.surname())
                .email(req.email())
                .password(passwordEncoder.encode(req.password()))
                .build();

        return accountRepo.save(account);
    }

    @Override
    public Account createAdminAccount(RegisterAccountRequest req) {
        var account = createAccount(req);

        account.setRole(UserRole.ADMIN);

        return accountRepo.save(account);
    }

    @Override
    public Account createGuestAccount(Account account) {
        return accountRepo.save(account);
    }

    @Override
    public Account updateAccount(String email, UpdateAccountRequest req) {
        var account = accountRepo.findByEmail(email).orElseThrow(() -> new EntityNotFoundException("Account does not exist"));

        account.setForename(req.forename());
        account.setSurname(req.surname());

        return accountRepo.save(account);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Account> findByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Boolean existsByEmail(String email) {
        return accountRepo.existsByEmail(email);
    }
}
