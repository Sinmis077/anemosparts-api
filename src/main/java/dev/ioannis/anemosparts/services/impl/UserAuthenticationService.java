package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.security.auth.login.AccountNotFoundException;
import java.util.List;

@Service
@Transactional(readOnly = true)
@AllArgsConstructor
public class UserAuthenticationService implements UserDetailsService {
    private final AccountService accountService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var account = accountService.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        // If an account is a "guest" account (AKA passwordless) then it doesn't exist
        if(account.getPassword() == null) throw new UsernameNotFoundException(username);

        return new User(
                account.getEmail(),
                account.getPassword(),
                List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
        );
    }

    public String login(String email, String password) throws AccountNotFoundException, BadCredentialsException {
        var account = accountService.findByEmail(email).orElseThrow(
                () -> new AccountNotFoundException("Account doesn't exist")
        );

        if(account.getPassword() == null) throw new AccountNotFoundException("Account doesn't exist");
        if(!passwordEncoder.matches(password, account.getPassword()))
            throw new BadCredentialsException("Wrong login credentials please try again");

        return jwtService.generateToken(account);
    }
}
