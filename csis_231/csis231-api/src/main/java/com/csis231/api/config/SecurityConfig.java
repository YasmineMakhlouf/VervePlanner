package com.csis231.api.config;

import com.csis231.api.auth.CustomUserDetailsService;
import com.csis231.api.auth.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Security configuration for the backend application.
 *
 * Responsibilities:
 * - Configure Spring Security for JWT-based authentication.
 * - Disable session usage (stateless API).
 * - Register authentication filters and access rules.
 * - Provide PasswordEncoder and AuthenticationManager beans.
 *
 * Security Implementation Details:
 * - All `/api/auth/**` endpoints are public (login/register/validate).
 * - All `/api/customers/**` require an authenticated JWT.
 * - Other endpoints are allowed by default but can be restricted later.
 *
 * JWT Integration:
 * - JwtAuthenticationFilter is injected into the filter chain BEFORE
 *   UsernamePasswordAuthenticationFilter to validate tokens on every request.
 *
 * State Management:
 * - Stateless sessions ensure the backend never stores user session data.
 * - Authentication is fully determined by the incoming JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    /**
     * Password hashing bean used for encoding user passwords.
     *
     * @return BCryptPasswordEncoder instance
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Exposes the AuthenticationManager used by Spring Security.
     *
     * @param config the AuthenticationConfiguration provided by Spring
     * @return AuthenticationManager instance
     * @throws Exception if authentication manager initialization fails
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the security filter chain for HTTP requests.
     *
     * Configuration includes:
     * - Disabling CSRF for stateless REST APIs.
     * - Using stateless session policy.
     * - Defining endpoint authorization rules.
     * - Registering the JWT authentication filter.
     *
     * @param http HttpSecurity object provided by Spring Security
     * @return built SecurityFilterChain instance
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers("/api/customers/**").authenticated()
                        .anyRequest().permitAll()
                );

        // Register JWT filter before Spring's default authentication filter
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
