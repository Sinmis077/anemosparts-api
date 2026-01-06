package dev.ioannis.anemosparts.services.impl;

import dev.ioannis.anemosparts.entities.Account;
import dev.ioannis.anemosparts.services.AccountService;
import dev.ioannis.anemosparts.services.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.Date;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class JwtServiceImpl implements JwtService {

    private final AccountService accountService;

    @Value("${app.security.jwt.secret}")
    private String secret;

    @Value("${app.security.jwt.expiry}")
    private Long expiryTime;

    private SecretKey secretKey;

    @PostConstruct
    private void init() {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    @Override
    public String generateToken(Account account) {
        return Jwts.builder()
                .subject(account.getEmail())
                .claim("role", account.getRole().name())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiryTime))
                .signWith(secretKey)
                .compact();
    }

    @Override
    public Boolean isTokenValid(String token) {
        var claims = getClaimsFromToken(token);

        if (claims == null) return false;

        var account = accountService.findByEmail(claims.getPayload().getSubject());
        return account.isPresent() && account.get().getPassword() != null;
    }

    @Override
    public String extractEmailFromToken(String token) {
        var claims = getClaimsFromToken(token);

        if (claims == null) return null;
        return claims.getPayload().getSubject();
    }

    private Jws<Claims> getClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .clockSkewSeconds(60L)
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token);
        } catch (JwtException e) {
            log.debug("Attempted to parse a token we did not provide: {}", e.getMessage());
            return null;
        }
    }
}
