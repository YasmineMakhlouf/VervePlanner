package com.csis231.api.planner;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planners")
public class PlannerController {
    private final PlannerService service;

    public PlannerController(PlannerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Planner> all() {
        return service.getAllPlanners();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Planner create(@Valid @RequestBody Planner planner) {
        return service.createPlanner(planner);
    }

    @GetMapping("/{id}")
    public Planner get(@PathVariable Long id) {
        return service.getPlannerById(id);
    }

    @PutMapping("/{id}")
    public Planner update(@PathVariable Long id, @Valid @RequestBody Planner planner) {
        return service.updatePlanner(id, planner);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deletePlanner(id);
    }
}
