package com.csis231.api.auth;

import com.csis231.api.user.User;
import com.csis231.api.user.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    // Admin email automatically receives ADMIN role
    private static final String ADMIN_EMAIL = "admin@gmail.com";

    /**
     * Registers a new user.
     *
     * Centralized state management:
     * - Ensures unique username and email.
     * - Stores password securely using PasswordEncoder.
     * - Assigns role based on email (ADMIN for admin@gmail.com, USER otherwise).
     * - Generates JWT token for immediate authentication.
     *
     * Security & Validation:
     * - Username and email must be unique.
     * - Password is hashed before storage.
     * - Instant timestamp records creation time.
     *
     * @param request RegisterRequest containing username, email, password
     * @return AuthResponse containing JWT token and user info
     * @throws RuntimeException if username/email already exists
     */
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email is already in use!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getEmail().equalsIgnoreCase(ADMIN_EMAIL) ? "ADMIN" : "USER");
        user.setCreatedAt(Instant.now());

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getUsername());

        return new AuthResponse(
                token,
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    /**
     * Logs in a user.
     *
     * Centralized state management:
     * - Authenticates via Spring Security AuthenticationManager.
     * - Fetches user info from repository.
     * - Updates ADMIN role if admin email is used.
     * - Generates JWT token for session management.
     *
     * Security & Validation:
     * - Password verification is handled by AuthenticationManager.
     * - Throws exception if user not found.
     * - ADMIN role automatically enforced for admin@gmail.com.
     *
     * @param request LoginRequest containing username and password
     * @return AuthResponse containing JWT token and user info
     * @throws RuntimeException if user not found
     */
    public AuthResponse login(@Valid LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getEmail().equalsIgnoreCase(ADMIN_EMAIL) && !"ADMIN".equals(user.getRole())) {
            user.setRole("ADMIN");
            userRepository.save(user);
        }

        String token = jwtUtil.generateToken(user.getUsername());

        return new AuthResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    /**
     * Validates a JWT token.
     *
     * Centralized state management:
     * - Delegates token verification to JwtUtil.
     * - Ensures only valid tokens are accepted for secured endpoints.
     *
     * Security & Validation:
     * - Checks signature, expiration, and format of JWT.
     *
     * @param token JWT token string
     * @return true if token is valid, false otherwise
     */
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token);
    }
}
