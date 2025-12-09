package com.csis231.api.event;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller that exposes CRUD endpoints for managing Event resources.
 *
 * Responsibilities:
 * - Handle incoming HTTP requests for events
 * - Validate request bodies using @Valid
 * - Delegate business logic to EventService
 * - Return appropriate HTTP responses and status codes
 */
@RestController
@RequestMapping("/api/events")
public class EventController {

    private final EventService service;

    /**
     * Injects the EventService used to perform business logic.
     *
     * @param service EventService instance
     */
    public EventController(EventService service) {
        this.service = service;
    }

    /**
     * GET /api/events
     *
     * Retrieves all events stored in the system.
     *
     * @return List of Event objects
     */
    @GetMapping
    public List<Event> all() {
        return service.getAllEvents();
    }

    /**
     * POST /api/events
     *
     * Creates a new event. The request body is validated.
     *
     * @param event The event to be created, validated using @Valid
     * @return The created event
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Event create(@Valid @RequestBody Event event) {
        return service.createEvent(event);
    }

    /**
     * GET /api/events/{id}
     *
     * Fetches an event by its unique ID.
     *
     * @param id The ID of the event
     * @return The Event with the matching ID
     */
    @GetMapping("/{id}")
    public Event get(@PathVariable Long id) {
        return service.getEventById(id);
    }

    /**
     * PUT /api/events/{id}
     *
     * Updates an existing event. If the event does not exist, the service throws an exception.
     * The request body is validated.
     *
     * @param id The ID of the event to update
     * @param event The updated event data
     * @return The updated Event object
     */
    @PutMapping("/{id}")
    public Event update(@PathVariable Long id, @Valid @RequestBody Event event) {
        return service.updateEvent(id, event);
    }

    /**
     * DELETE /api/events/{id}
     *
     * Deletes an event by ID.
     * If no event is found, the service throws an exception.
     *
     * @param id The ID of the event to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteEvent(id);
    }
}
