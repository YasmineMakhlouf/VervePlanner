package com.csis231.api.chat;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller responsible for managing Chat entities.
 * Provides CRUD operations via RESTful endpoints.
 *
 * Centralized State:
 * - Delegates all business logic to ChatService.
 * - Controller only handles HTTP-level concerns (routing, validation, status codes).
 *
 * Security & Validation:
 * - @Valid ensures request bodies are validated before service calls.
 * - Any security (JWT, filters) is applied globally via Spring Security, not here.
 */
@RestController
@RequestMapping("/api/chats")
public class ChatController {

    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    /**
     * Retrieves all chats from the database.
     *
     * @return List of all Chat objects
     */
    @GetMapping
    public List<Chat> all() {
        return service.getAllChats();
    }

    /**
     * Creates a new Chat entry.
     *
     * @param chat Chat object containing validated data from the request body
     * @return the newly created Chat
     *
     * HTTP 201 indicates successful resource creation.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chat create(@Valid @RequestBody Chat chat) {
        return service.createChat(chat);
    }

    /**
     * Retrieves a single Chat by its ID.
     *
     * @param id ID of the chat to fetch
     * @return the Chat object if found
     */
    @GetMapping("/{id}")
    public Chat get(@PathVariable Long id) {
        return service.getChatById(id);
    }

    /**
     * Updates an existing Chat.
     *
     * @param id   ID of the chat to update
     * @param chat Updated chat data from request body
     * @return updated Chat object
     */
    @PutMapping("/{id}")
    public Chat update(@PathVariable Long id, @Valid @RequestBody Chat chat) {
        return service.updateChat(id, chat);
    }

    /**
     * Deletes a Chat by ID.
     *
     * @param id ID of the chat to delete
     *
     * HTTP 204 indicates successful deletion with no response body.
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteChat(id);
    }
}
