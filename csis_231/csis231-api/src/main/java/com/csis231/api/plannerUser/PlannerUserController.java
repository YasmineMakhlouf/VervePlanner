package com.csis231.api.plannerUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planner-users")
public class PlannerUserController {

    private final PlannerUserRepository repo;

    public PlannerUserController(PlannerUserRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<PlannerUser> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlannerUser create(@Valid @RequestBody PlannerUser plannerUser) {
        return repo.save(plannerUser);
    }

    @GetMapping("/{id}")
    public PlannerUser get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));
    }

    @PutMapping("/{id}")
    public PlannerUser update(@PathVariable Long id, @Valid @RequestBody PlannerUser plannerUser) {
        PlannerUser existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("PlannerUser not found"));

        existing.setPlannerId(plannerUser.getPlannerId());
        existing.setUserId(plannerUser.getUserId());
        existing.setRole(plannerUser.getRole());

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}