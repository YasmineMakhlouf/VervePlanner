package com.csis231.api.plannerUser;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerUserService {
    private final PlannerUserRepository repo;

    public PlannerUserService(PlannerUserRepository repo) {
        this.repo = repo;
    }

    public List<PlannerUser> getAllPlannerUsers() {
        return repo.findAll();
    }

    public PlannerUser getPlannerUserById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));
    }

    public PlannerUser createPlannerUser(PlannerUser plannerUser) {
        return repo.save(plannerUser);
    }

    public PlannerUser updatePlannerUser(Long id, PlannerUser plannerUser) {
        PlannerUser existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));

        existing.setPlannerId(plannerUser.getPlannerId());
        existing.setUserId(plannerUser.getUserId());
        existing.setRole(plannerUser.getRole());

        return repo.save(existing);
    }

    public void deletePlannerUser(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("PlannerUser not found");
        }
        repo.deleteById(id);
    }
}
