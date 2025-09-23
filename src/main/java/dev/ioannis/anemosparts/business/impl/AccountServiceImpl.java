package dev.ioannis.anemosparts.business.impl;

import dev.ioannis.anemosparts.business.AccountService;
import dev.ioannis.anemosparts.domain.Account;
import dev.ioannis.anemosparts.domain.entity.AccountEntity;
import dev.ioannis.anemosparts.mapper.AccountMapper;
import dev.ioannis.anemosparts.persistance.AccountRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    public AccountRepo accountRepo;

    @Override
    public Set<Account> findAll() {
        Set<Account> accounts = new HashSet<>();
        for(AccountEntity accountEntity : accountRepo.findAll()) {
            accounts.add(AccountMapper.convertToAccount(accountEntity));
        }
        return accounts;
    }

    @Override
    public Account findByEmail(String email) {
        AccountEntity accountEntity = accountRepo.findById(email).orElse(null);

        if(accountEntity == null) {
            return null;
        }
        return AccountMapper.convertToAccount(accountEntity);
    }

    @Override
    public Boolean login(Account account) {
        AccountEntity accountEntity = accountRepo.findById(account.getEmail()).orElse(null);

        return accountEntity != null && !account.equals(AccountMapper.convertToAccount(accountEntity));
    }

    @Override
    public Account create(Account account) {
        AccountEntity accountEntity = accountRepo.save(AccountMapper.convertToAccountEntity(account));
        return AccountMapper.convertToAccount(accountEntity);
    }

    @Override
    public Boolean delete(Account account) {
        try {
            accountRepo.delete(AccountMapper.convertToAccountEntity(account));
            return true;
        } catch(Exception ex) {
            return false;
        }
    }
}
