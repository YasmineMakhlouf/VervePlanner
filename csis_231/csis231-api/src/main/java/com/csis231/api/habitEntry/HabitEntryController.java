package com.csis231.api.habitEntry;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/habit-entries")
public class HabitEntryController {
    private final HabitEntryService service;

    public HabitEntryController(HabitEntryService service) {
        this.service = service;
    }

    @GetMapping
    public List<HabitEntry> all() {
        return service.getAllEntries();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public HabitEntry create(@Valid @RequestBody HabitEntry habitEntry) {
        return service.createEntry(habitEntry);
    }

    @GetMapping("/{id}")
    public HabitEntry get(@PathVariable Long id) {
        return service.getEntryById(id);
    }

    @PutMapping("/{id}")
    public HabitEntry update(@PathVariable Long id, @Valid @RequestBody HabitEntry habitEntry) {
        return service.updateEntry(id, habitEntry);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteEntry(id);
    }
}
