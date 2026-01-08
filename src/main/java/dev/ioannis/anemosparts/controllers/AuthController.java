package dev.ioannis.anemosparts.controllers;

import dev.ioannis.anemosparts.domain.requests.LoginRequest;
import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.domain.responses.TokenResponse;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.impl.UserAuthenticationService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

import javax.security.auth.login.AccountNotFoundException;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserAuthenticationService userAuthenticationService;
    private final AccountService accountService;

    @Value("${app.security.jwt.expiry}")
    private String expiryTime;


    @PostMapping("/test")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTestAccount() {
        accountService.createAdminAccount(new RegisterAccountRequest("test", "testington", "test@gmail.com", "12345678"));
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@RequestBody @Valid LoginRequest req, HttpServletResponse response)
            throws AccountNotFoundException, BadCredentialsException {
        String token = userAuthenticationService.login(req.email(), req.password());
        response.addCookie(createCookie(token));

        return new TokenResponse(token);
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse register(@RequestBody @Valid RegisterAccountRequest req,
                                  HttpServletResponse response)
            throws AccountNotFoundException, BadCredentialsException {
        var account = accountService.createAccount(req);

        String token = userAuthenticationService.login(account.getEmail(), account.getPassword());
        response.addCookie(createCookie(token));

        return new TokenResponse(token);
    }

    @PostMapping("/register/admin")
    @ResponseStatus(HttpStatus.CREATED)
    public TokenResponse registerAdmin(@RequestBody @Valid RegisterAccountRequest req,
                                       HttpServletResponse response)
            throws AccountNotFoundException, BadCredentialsException {
        var account = accountService.createAdminAccount(req);

        String token = userAuthenticationService.login(account.getEmail(), account.getPassword());
        response.addCookie(createCookie(token));

        return new TokenResponse(userAuthenticationService.login(account.getEmail(), account.getPassword()));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAuthCookie(HttpServletResponse response) {
        var cookie = createCookie(null);
        cookie.setMaxAge(0);

        response.addCookie(cookie);
    }

    private Cookie createCookie(String token) {
        var cookie = new Cookie("anemosparts-authorization", token);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setMaxAge((int)(Long.parseLong(expiryTime) / 1000));
        cookie.setAttribute("sameSite", "strict");

        return cookie;
    }
}
