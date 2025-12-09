package com.csis231.api.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Message entity.
 *
 * Handles all HTTP requests related to messages:
 * - Retrieve all messages
 * - Retrieve a message by ID
 * - Create a new message
 * - Update an existing message
 * - Delete a message
 *
 * Interacts with MessageService for business logic.
 */
@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the MessageService instance used for business logic
     */
    public MessageController(MessageService service) {
        this.service = service;
    }

    /**
     * Retrieve all messages.
     *
     * @return a list of all Message objects
     */
    @GetMapping
    public List<Message> all() {
        return service.getAllMessages();
    }

    /**
     * Create a new message.
     *
     * @param message the Message object to create
     * @return the saved Message object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@Valid @RequestBody Message message) {
        return service.createMessage(message);
    }

    /**
     * Retrieve a message by its ID.
     *
     * @param id the ID of the message to retrieve
     * @return the Message object with the given ID
     */
    @GetMapping("/{id}")
    public Message get(@PathVariable Long id) {
        return service.getMessageById(id);
    }

    /**
     * Update an existing message.
     *
     * @param id      the ID of the message to update
     * @param message the Message object containing updated fields
     * @return the updated Message object
     */
    @PutMapping("/{id}")
    public Message update(@PathVariable Long id, @Valid @RequestBody Message message) {
        return service.updateMessage(id, message);
    }

    /**
     * Delete a message by its ID.
     *
     * @param id the ID of the message to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteMessage(id);
    }
}
