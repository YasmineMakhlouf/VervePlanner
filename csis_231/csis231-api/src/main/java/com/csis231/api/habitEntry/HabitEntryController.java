package com.csis231.api.habitEntry;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habit-entries")
public class HabitEntryController {
    private final HabitEntryRepository repo;
    public HabitEntryController(HabitEntryRepository repo) { this.repo = repo; }

    @GetMapping
    public List<HabitEntry> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitEntry create(@Valid @RequestBody HabitEntry habitEntry) {
        return repo.save(habitEntry);
    }

    @GetMapping("/{id}")
    public HabitEntry get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Habit entry not found"));
    }

    @PutMapping("/{id}")
    public HabitEntry update(@PathVariable Long id, @Valid @RequestBody HabitEntry habitEntry) {
        HabitEntry existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Habit entry not found"));
        existing.setHabitId(habitEntry.getHabitId());
        existing.setEntryDate(habitEntry.getEntryDate());
        existing.setCompleted(habitEntry.getCompleted());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}