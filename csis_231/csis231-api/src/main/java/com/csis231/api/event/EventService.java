package com.csis231.api.event;

import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer responsible for handling business logic related to Event operations.
 *
 * Responsibilities:
 * - Communicate with the EventRepository (data layer)
 * - Validate that events exist before update/delete operations
 * - Apply updates safely to existing entities
 */
@Service
public class EventService {

    private final EventRepository repo;

    /**
     * Constructor-based dependency injection for EventRepository.
     *
     * @param repo the EventRepository used for database access
     */
    public EventService(EventRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all events from the database.
     *
     * @return a list of all Event objects
     */
    public List<Event> getAllEvents() {
        return repo.findAll();
    }

    /**
     * Retrieves a single event by ID.
     * If the event does not exist, a RuntimeException is thrown.
     *
     * @param id the ID of the event
     * @return the Event object with the given ID
     */
    public Event getEventById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Event not found"));
    }

    /**
     * Creates and saves a new event in the database.
     *
     * @param event the Event object to be created
     * @return the saved Event object
     */
    public Event createEvent(Event event) {
        return repo.save(event);
    }

    /**
     * Updates an existing event.
     * This method first validates that the event exists, then copies
     * updated fields before saving the modified entity.
     *
     * @param id    the ID of the event to update
     * @param event the updated event data
     * @return the updated Event object
     */
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

    /**
     * Deletes an event by ID.
     * Validates the ID before deleting.
     *
     * @param id the ID of the event to delete
     */
    public void deleteEvent(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Event not found");
        }
        repo.deleteById(id);
    }
}
