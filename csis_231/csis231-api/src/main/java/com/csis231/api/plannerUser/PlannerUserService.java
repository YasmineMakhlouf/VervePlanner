package com.csis231.api.plannerUser;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for PlannerUser entity.
 *
 * Handles business logic for CRUD operations on planner users.
 */
@Service
public class PlannerUserService {

    private final PlannerUserRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the PlannerUserRepository for database access
     */
    public PlannerUserService(PlannerUserRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all planner users.
     *
     * @return list of all PlannerUser objects
     */
    public List<PlannerUser> getAllPlannerUsers() {
        return repo.findAll();
    }

    /**
     * Retrieve a planner user by ID.
     *
     * @param id the ID of the planner user
     * @return the PlannerUser object with the given ID
     */
    public PlannerUser getPlannerUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));
    }

    /**
     * Create a new planner user.
     *
     * @param plannerUser the PlannerUser object to create
     * @return the created PlannerUser object
     */
    public PlannerUser createPlannerUser(PlannerUser plannerUser) {
        return repo.save(plannerUser);
    }

    /**
     * Update an existing planner user by ID.
     *
     * @param id          the ID of the planner user to update
     * @param plannerUser the PlannerUser object containing updated fields
     * @return the updated PlannerUser object
     */
    public PlannerUser updatePlannerUser(Long id, PlannerUser plannerUser) {
        PlannerUser existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));

        existing.setPlannerId(plannerUser.getPlannerId());
        existing.setUserId(plannerUser.getUserId());
        existing.setRole(plannerUser.getRole());

        return repo.save(existing);
    }

    /**
     * Delete a planner user by ID.
     *
     * @param id the ID of the planner user to delete
     */
    public void deletePlannerUser(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("PlannerUser not found");
        }
        repo.deleteById(id);
    }
}
