package com.csis231.api.planner;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlannerService {
    private final PlannerRepository repo;

    public PlannerService(PlannerRepository repo) {
        this.repo = repo;
    }

    public List<Planner> getAllPlanners() {
        return repo.findAll();
    }

    public Planner getPlannerById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
    }

    public Planner createPlanner(Planner planner) {
        return repo.save(planner);
    }

    public Planner updatePlanner(Long id, Planner planner) {
        Planner existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Planner not found"));
        existing.setOwnerId(planner.getOwnerId());
        existing.setName(planner.getName());
        existing.setType(planner.getType());
        existing.setPasswordHash(planner.getPasswordHash());
        return repo.save(existing);
    }

    public void deletePlanner(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Planner not found");
        }
        repo.deleteById(id);
    }
}
