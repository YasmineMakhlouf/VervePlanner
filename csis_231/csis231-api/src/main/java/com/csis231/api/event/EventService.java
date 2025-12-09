package com.csis231.api.event;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventService {
    private final EventRepository repo;

    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    public Event getEventById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    public Event createEvent(Event event) {
        return repo.save(event);
    }

    public Event updateEvent(Long id, Event event) {
        Event existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
        existing.setPlannerId(event.getPlannerId());
        existing.setTitle(event.getTitle());
        existing.setDescription(event.getDescription());
        existing.setStartDatetime(event.getStartDatetime());
        existing.setEndDatetime(event.getEndDatetime());
        existing.setLocation(event.getLocation());
        return repo.save(existing);
    }

    public void deleteEvent(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        repo.deleteById(id);
    }
}
