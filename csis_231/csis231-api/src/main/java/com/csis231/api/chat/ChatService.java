package com.csis231.api.chat;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service layer for managing Chat entities.
 *
 * Responsibilities:
 * - Provides business logic and database interaction for Chat operations.
 * - Ensures clean separation between controllers (HTTP logic) and persistence.
 *
 * State Management:
 * - Centralized here: controller never touches the repository directly.
 * - Any future caching/state tracking should be added at this layer.
 *
 * Validation & Error Handling:
 * - Throws RuntimeException when chats are not found.
 * - Controller or global @ControllerAdvice can convert these into HTTP errors.
 */
@Service
public class ChatService {

    private final ChatRepository repo;

    public ChatService(ChatRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all chat entries.
     *
     * @return List of Chat objects stored in the repository
     */
    public List<Chat> getAllChats() {
        return repo.findAll();
    }

    /**
     * Retrieves a Chat by its ID.
     *
     * @param id the ID of the chat to retrieve
     * @return Chat object if found
     * @throws RuntimeException if chat is not found
     */
    public Chat getChatById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    /**
     * Creates a new Chat entry.
     *
     * @param chat the Chat object to save
     * @return the saved Chat object
     */
    public Chat createChat(Chat chat) {
        return repo.save(chat);
    }

    /**
     * Updates an existing Chat entry.
     *
     * @param id   the ID of the chat to update
     * @param chat the Chat object containing updated data
     * @return the updated Chat entity
     * @throws RuntimeException if chat with the given ID does not exist
     */
    public Chat updateChat(Long id, Chat chat) {
        Chat existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));

        existing.setPlannerId(chat.getPlannerId());
        existing.setCreatedAt(chat.getCreatedAt());

        return repo.save(existing);
    }

    /**
     * Deletes a Chat entry by ID.
     *
     * @param id the ID of the chat to delete
     * @throws RuntimeException if the chat does not exist
     */
    public void deleteChat(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Chat not found");
        }
        repo.deleteById(id);
    }
}
