package com.csis231.api.habit;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habits")
public class HabitController {
    private final HabitRepository repo;
    public HabitController(HabitRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Habit> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Habit create(@Valid @RequestBody Habit habit) {
        return repo.save(habit);
    }

    @GetMapping("/{id}")
    public Habit get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Habit not found"));
    }

    @PutMapping("/{id}")
    public Habit update(@PathVariable Long id, @Valid @RequestBody Habit habit) {
        Habit existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Habit not found"));
        existing.setPlannerId(habit.getPlannerId());
        existing.setTitle(habit.getTitle());
        existing.setDescription(habit.getDescription());
        existing.setFrequency(habit.getFrequency());
        existing.setGoalCount(habit.getGoalCount());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}