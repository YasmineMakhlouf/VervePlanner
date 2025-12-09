package com.csis231.api.user;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

/**
 * Service layer for User entity.
 *
 * Handles all business logic and interactions with the UserRepository.
 */
@Service
public class UserService {

    private final UserRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the UserRepository instance
     */
    public UserService(UserRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all users from the database.
     *
     * @return a list of all User objects
     */
    public List<User> getAllUsers() {
        return repo.findAll();
    }

    /**
     * Retrieve a single user by ID.
     *
     * @param id the ID of the user
     * @return the User object with the given ID
     * @throws RuntimeException if user is not found
     */
    public User getUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    /**
     * Create a new user in the database.
     *
     * Automatically sets the creation timestamp and assigns role:
     * - "ADMIN" if the email is "admin@gmail.com"
     * - "USER" for all other emails
     *
     * @param user the User object to create
     * @return the created User object
     */
    public User createUser(User user) {
        user.setCreatedAt(Instant.now());

        if ("admin@gmail.com".equalsIgnoreCase(user.getEmail())) {
            user.setRole("ADMIN");
        } else {
            user.setRole("USER");
        }

        return repo.save(user);
    }

    /**
     * Update an existing user in the database.
     *
     * @param id   the ID of the user to update
     * @param user the User object containing updated fields
     * @return the updated User object
     * @throws RuntimeException if user is not found
     */
    public User updateUser(Long id, User user) {
        User existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        existing.setUsername(user.getUsername());
        existing.setEmail(user.getEmail());
        existing.setPasswordHash(user.getPasswordHash());
        existing.setRole(user.getRole());

        return repo.save(existing);
    }

    /**
     * Delete a user by ID from the database.
     *
     * @param id the ID of the user to delete
     * @throws RuntimeException if user is not found
     */
    public void deleteUser(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        repo.deleteById(id);
    }
}
