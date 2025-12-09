package com.csis231.api.planner;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Planner entity.
 *
 * Handles business logic and CRUD operations for planners.
 */
@Service
public class PlannerService {

    private final PlannerRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the PlannerRepository for database operations
     */
    public PlannerService(PlannerRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all planners.
     *
     * @return a list of all Planner objects
     */
    public List<Planner> getAllPlanners() {
        return repo.findAll();
    }

    /**
     * Get a planner by its ID.
     *
     * @param id the ID of the planner
     * @return the Planner object with the given ID
     * @throws RuntimeException if planner is not found
     */
    public Planner getPlannerById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
    }

    /**
     * Create a new planner.
     *
     * @param planner the Planner object to create
     * @return the created Planner object
     */
    public Planner createPlanner(Planner planner) {
        return repo.save(planner);
    }

    /**
     * Update an existing planner by its ID.
     *
     * @param id      the ID of the planner to update
     * @param planner the Planner object containing updated fields
     * @return the updated Planner object
     * @throws RuntimeException if planner is not found
     */
    public Planner updatePlanner(Long id, Planner planner) {
        Planner existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
        existing.setOwnerId(planner.getOwnerId());
        existing.setName(planner.getName());
        existing.setType(planner.getType());
        existing.setPasswordHash(planner.getPasswordHash());
        return repo.save(existing);
    }

    /**
     * Delete a planner by its ID.
     *
     * @param id the ID of the planner to delete
     * @throws RuntimeException if planner is not found
     */
    public void deletePlanner(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Planner not found");
        }
        repo.deleteById(id);
    }
}
