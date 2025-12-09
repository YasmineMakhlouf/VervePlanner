package com.csis231.api.auth;

import com.csis231.api.user.User;
import com.csis231.api.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Loads user details by username for Spring Security authentication.
     *
     * Centralized state management:
     * - Retrieves user from database using UserRepository.
     * - Maps user role to Spring Security authorities.
     *
     * Security & Validation:
     * - Throws UsernameNotFoundException if the username does not exist.
     * - Uses stored password hash for authentication verification.
     * - Sets account as active, non-expired, and non-locked (no custom isActive field).
     *
     * @param username the username of the user attempting to log in
     * @return UserDetails object containing username, password, and authorities
     * @throws UsernameNotFoundException if the user does not exist
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPasswordHash())
                .authorities(user.getRole())
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(false)
                .build();
    }
}
