package dev.ioannis.anemosparts.business;

import dev.ioannis.anemosparts.domain.Account;

import java.util.Set;

public interface AccountService {
    public Set<Account> findAll();
    public Account findByEmail(String email);
    public Boolean login(Account account);
    public Account create(Account account);
    public Boolean delete(Account account);
}
