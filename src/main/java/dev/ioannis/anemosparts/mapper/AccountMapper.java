package dev.ioannis.anemosparts.mapper;

import dev.ioannis.anemosparts.domain.Account;
import dev.ioannis.anemosparts.domain.entity.AccountEntity;

public class AccountMapper {
    public static Account convertToAccount(AccountEntity account) {
        return new Account(account.getEmail(), account.getPassword());
    }

    public static AccountEntity convertToAccountEntity(Account account) {
        return new AccountEntity(account.getEmail(), account.getPassword());
    }
}
