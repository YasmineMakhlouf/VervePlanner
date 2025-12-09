package com.csis231.api.chat;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatService {
    private final ChatRepository repo;

    public ChatService(ChatRepository repo) {
        this.repo = repo;
    }

    public List<Chat> getAllChats() {
        return repo.findAll();
    }

    public Chat getChatById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    public Chat createChat(Chat chat) {
        return repo.save(chat);
    }

    public Chat updateChat(Long id, Chat chat) {
        Chat existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        existing.setPlannerId(chat.getPlannerId());
        existing.setCreatedAt(chat.getCreatedAt());
        return repo.save(existing);
    }

    public void deleteChat(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Chat not found");
        }
        repo.deleteById(id);
    }
}
