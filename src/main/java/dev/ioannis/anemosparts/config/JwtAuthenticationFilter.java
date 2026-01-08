package dev.ioannis.anemosparts.config;

import dev.ioannis.anemosparts.services.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService authenticationService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var cookies = request.getCookies();

        if(cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var authCookie = Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equals("anemosparts-authorization"))
                .findFirst().orElse(null);

        if (authCookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authCookie.getValue();

        if(token == null || token.isBlank()) {
            filterChain.doFilter(request, response);
            return;
        }

        String email = jwtService.extractEmailFromToken(token);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = authenticationService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token)) {
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
