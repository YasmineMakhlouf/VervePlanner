package com.csis231.api.user;

import com.csis231.api.auth.LoginRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller for User entity.
 *
 * Handles HTTP requests for CRUD operations on users.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the UserService instance
     */
    public UserController(UserService service) {
        this.service = service;
    }

    /**
     * Retrieve all users.
     *
     * @return a list of all User objects
     */
    @GetMapping
    public List<User> all() {
        return service.getAllUsers();
    }

    /**
     * Create a new user.
     *
     * @param user the User object to create
     * @return the created User object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public User create(@Valid @RequestBody User user) {
        return service.createUser(user);
    }

    /**
     * Get a single user by ID.
     *
     * @param id the ID of the user
     * @return the User object with the given ID
     */
    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return service.getUserById(id);
    }

    /**
     * Update an existing user.
     *
     * @param id   the ID of the user to update
     * @param user the User object containing updated fields
     * @return the updated User object
     */
    @PutMapping("/{id}")
    public User update(@PathVariable Long id, @Valid @RequestBody User user) {
        return service.updateUser(id, user);
    }

    /**
     * Delete a user by ID.
     *
     * @param id the ID of the user to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteUser(id);
    }
}
