package com.csis231.api.event;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/events")
public class EventController {
    private final EventRepository repo;
    public EventController(EventRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Event> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody Event event) {
        return repo.save(event);
    }

    @GetMapping("/{id}")
    public Event get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
    }

    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody Event event) {
        Event existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Event not found"));
        existing.setPlannerId(event.getPlannerId());
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setStartDatetime(event.getStartDatetime());
        existing.setEndDatetime(event.getEndDatetime());
        existing.setLocation(event.getLocation());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}