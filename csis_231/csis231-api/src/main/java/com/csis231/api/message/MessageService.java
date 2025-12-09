package com.csis231.api.message;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for Message entity.
 *
 * Handles business logic and CRUD operations for messages.
 * Interacts with MessageRepository for database persistence.
 */
@Service
public class MessageService {

    private final MessageRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the MessageRepository instance used for database operations
     */
    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all messages from the repository.
     *
     * @return a list of all Message objects
     */
    public List<Message> getAllMessages() {
        return repo.findAll();
    }

    /**
     * Retrieve a message by its ID.
     *
     * @param id the ID of the message to retrieve
     * @return the Message object with the given ID
     * @throws RuntimeException if no message is found with the specified ID
     */
    public Message getMessageById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    /**
     * Create and save a new message.
     *
     * @param message the Message object to create
     * @return the saved Message object
     */
    public Message createMessage(Message message) {
        return repo.save(message);
    }

    /**
     * Update an existing message by its ID.
     *
     * @param id      the ID of the message to update
     * @param message the Message object containing updated fields
     * @return the updated Message object
     * @throws RuntimeException if no message is found with the specified ID
     */
    public Message updateMessage(Long id, Message message) {
        Message existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        existing.setChatId(message.getChatId());
        existing.setSenderId(message.getSenderId());
        existing.setContent(message.getContent());
        return repo.save(existing);
    }

    /**
     * Delete a message by its ID.
     *
     * @param id the ID of the message to delete
     * @throws RuntimeException if no message is found with the specified ID
     */
    public void deleteMessage(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Message not found");
        }
        repo.deleteById(id);
    }
}
