package dev.ioannis.anemosparts.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Value("${app.dns.origin}")
    private String dnsOrigin;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .cors(Customizer.withDefaults())
            .sessionManagement(session ->
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.GET,
                                "/api/parts/**",
                                "/api/models/**",
                                "/api/brands/**",
                                "/resources/images/**"
                        ).permitAll()
                        .requestMatchers("/api/checkout/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/auth/register/admin").hasRole("ADMIN")
                        .requestMatchers("/v3/api-docs/**").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/orders/**")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/parts/**", "/api/models/**", "/api/brands/**", "/api/images/**")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/parts/**", "/api/models/**", "/api/brands/**", "/api/orders/**")
                            .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/parts/**", "/api/models/**", "/api/brands/**", "/api/orders/**")
                            .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // Relatively light CORS filter, ideally it would have far more controlled CORS
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        return request -> {
            CorsConfiguration configuration = new CorsConfiguration();

            configuration.addAllowedOrigin("http://localhost:5173");
            configuration.addAllowedOrigin("http://localhost:5174");
            configuration.addAllowedOrigin(dnsOrigin);

            configuration.addAllowedMethod("*");
            configuration.addAllowedHeader("*");

            configuration.setAllowCredentials(true);
            configuration.setAllowPrivateNetwork(true);

            return configuration;
        };
    }
}

