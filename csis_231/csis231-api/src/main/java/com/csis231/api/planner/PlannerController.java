package com.csis231.api.planner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Planner entity.
 *
 * Handles HTTP requests for CRUD operations on planners.
 */
@RestController
@RequestMapping("/api/planners")
public class PlannerController {

    private final PlannerService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the PlannerService used for business logic
     */
    public PlannerController(PlannerService service) {
        this.service = service;
    }

    /**
     * Get all planners.
     *
     * @return a list of all Planner objects
     */
    @GetMapping
    public List<Planner> all() {
        return service.getAllPlanners();
    }

    /**
     * Create a new planner.
     *
     * @param planner the Planner object to create
     * @return the created Planner object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planner create(@Valid @RequestBody Planner planner) {
        return service.createPlanner(planner);
    }

    /**
     * Retrieve a planner by its ID.
     *
     * @param id the ID of the planner to retrieve
     * @return the Planner object with the given ID
     */
    @GetMapping("/{id}")
    public Planner get(@PathVariable Long id) {
        return service.getPlannerById(id);
    }

    /**
     * Update an existing planner by its ID.
     *
     * @param id      the ID of the planner to update
     * @param planner the Planner object containing updated fields
     * @return the updated Planner object
     */
    @PutMapping("/{id}")
    public Planner update(@PathVariable Long id, @Valid @RequestBody Planner planner) {
        return service.updatePlanner(id, planner);
    }

    /**
     * Delete a planner by its ID.
     *
     * @param id the ID of the planner to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deletePlanner(id);
    }
}
