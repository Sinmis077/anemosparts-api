package dev.ioannis.anemosparts.controller;

import dev.ioannis.anemosparts.business.AccountService;
import dev.ioannis.anemosparts.domain.Account;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("all")
    public ResponseEntity<Set<Account>> getAllAccounts() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("")
    public ResponseEntity<Account> getAccountsByEmail(@RequestParam String email) {
        Account account = accountService.findByEmail(email);
        if(account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("login")
    public ResponseEntity<Boolean> login(@RequestBody Account account) {
        if(account == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(accountService.login(account));
    }

    @PostMapping("create")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        return ResponseEntity.ok(accountService.create(account));
    }

    @DeleteMapping("delete")
    public ResponseEntity<Account> deleteAccount(@RequestBody Account account) {
        if(accountService.delete(account)) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.notFound().build();
    }

}
