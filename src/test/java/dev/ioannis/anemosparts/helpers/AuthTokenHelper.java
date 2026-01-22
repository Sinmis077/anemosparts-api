package dev.ioannis.anemosparts.helpers;

import dev.ioannis.anemosparts.domain.requests.RegisterAccountRequest;
import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.JwtService;
import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuthTokenHelper {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private AccountService accountService;

    public Cookie adminCookie() {
        String token = jwtService.generateToken(generateAdmin());
        return new Cookie("anemosparts-authorization", token);
    }

    private Account generateAdmin() {
        return accountService.findByEmail("test@gmail.com").orElseGet(() ->
                accountService.createAdminAccount(
                        new RegisterAccountRequest(
                                "Test",
                                "Testington",
                                "test@gmail.com",
                                "12345678"
                        )
                )
        );
    }
}
