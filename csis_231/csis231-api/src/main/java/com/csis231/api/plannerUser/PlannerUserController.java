package com.csis231.api.plannerUser;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/planner-users")
public class PlannerUserController {
    private final PlannerUserService service;

    public PlannerUserController(PlannerUserService service) {
        this.service = service;
    }

    @GetMapping
    public List<PlannerUser> all() {
        return service.getAllPlannerUsers();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PlannerUser create(@Valid @RequestBody PlannerUser plannerUser) {
        return service.createPlannerUser(plannerUser);
    }

    @GetMapping("/{id}")
    public PlannerUser get(@PathVariable Long id) {
        return service.getPlannerUserById(id);
    }

    @PutMapping("/{id}")
    public PlannerUser update(@PathVariable Long id, @Valid @RequestBody PlannerUser plannerUser) {
        return service.updatePlannerUser(id, plannerUser);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deletePlannerUser(id);
    }
}
