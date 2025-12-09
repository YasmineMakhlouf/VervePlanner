package com.csis231.api.plannerUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for PlannerUser entity.
 *
 * Handles HTTP requests for CRUD operations on planner users.
 */
@RestController
@RequestMapping("/api/planner-users")
public class PlannerUserController {

    private final PlannerUserService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the PlannerUserService for business logic
     */
    public PlannerUserController(PlannerUserService service) {
        this.service = service;
    }

    /**
     * Retrieve all planner users.
     *
     * @return list of all PlannerUser objects
     */
    @GetMapping
    public List<PlannerUser> all() {
        return service.getAllPlannerUsers();
    }

    /**
     * Create a new planner user.
     *
     * @param plannerUser the PlannerUser object to create
     * @return the created PlannerUser object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlannerUser create(@Valid @RequestBody PlannerUser plannerUser) {
        return service.createPlannerUser(plannerUser);
    }

    /**
     * Retrieve a planner user by ID.
     *
     * @param id the ID of the planner user
     * @return the PlannerUser object with the given ID
     */
    @GetMapping("/{id}")
    public PlannerUser get(@PathVariable Long id) {
        return service.getPlannerUserById(id);
    }

    /**
     * Update an existing planner user by ID.
     *
     * @param id          the ID of the planner user to update
     * @param plannerUser the PlannerUser object containing updated fields
     * @return the updated PlannerUser object
     */
    @PutMapping("/{id}")
    public PlannerUser update(@PathVariable Long id, @Valid @RequestBody PlannerUser plannerUser) {
        return service.updatePlannerUser(id, plannerUser);
    }

    /**
     * Delete a planner user by ID.
     *
     * @param id the ID of the planner user to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deletePlannerUser(id);
    }
}
