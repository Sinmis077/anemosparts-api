package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.repositories.AccountRepo;
import dev.ioannis.anemosparts.services.AccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepo accountRepo;

    @Override
    public Account createAccount(Account account) {
        return accountRepo.save(account);
    }

    @Override
    public Optional<Account> findByEmail(String email) {
        return accountRepo.findByEmail(email);
    }

    @Override
    public Boolean existsByEmail(String email) {
        return accountRepo.existsByEmail(email);
    }
}
