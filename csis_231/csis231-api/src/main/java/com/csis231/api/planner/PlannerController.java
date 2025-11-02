package com.csis231.api.planner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planners")
public class PlannerController {
    private final PlannerRepository repo;
    public PlannerController(PlannerRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Planner> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planner create(@Valid @RequestBody Planner planner) {
        return repo.save(planner);
    }

    @GetMapping("/{id}")
    public Planner get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Planner not found"));
    }

    @PutMapping("/{id}")
    public Planner update(@PathVariable Long id, @Valid @RequestBody Planner planner) {
        Planner existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Planner not found"));
        existing.setOwnerId(planner.getOwnerId());
        existing.setName(planner.getName());
        existing.setType(planner.getType());
        existing.setPasswordHash(planner.getPasswordHash());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}