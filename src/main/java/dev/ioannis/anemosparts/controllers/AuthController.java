package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.LoginRequest;
import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.domain.responses.TokenResponse;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.impl.UserAuthenticationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {
    private final UserAuthenticationService userAuthenticationService;
    private final AccountService accountService;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody @Valid LoginRequest req)
            throws AccountNotFoundException, BadCredentialsException {
        return new TokenResponse(userAuthenticationService.login(req.email(), req.password()));
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@RequestBody @Valid RegisterAccountRequest req)
            throws AccountNotFoundException, BadCredentialsException {
        var account = accountService.createAccount(req);
        return new TokenResponse(userAuthenticationService.login(account.getEmail(), account.getPassword()));
    }

    @PostMapping("/register/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse registerAdmin(@RequestBody @Valid RegisterAccountRequest req)
            throws AccountNotFoundException, BadCredentialsException {
        var account = accountService.createAdminAccount(req);
        return new TokenResponse(userAuthenticationService.login(account.getEmail(), account.getPassword()));
    }

}
