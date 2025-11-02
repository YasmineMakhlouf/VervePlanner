package com.csis231.api.chat;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatRepository repo;

    public ChatController(ChatRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Chat> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chat create(@Valid @RequestBody Chat chat) {
        return repo.save(chat);
    }

    @GetMapping("/{id}")
    public Chat get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
    }

    @PutMapping("/{id}")
    public Chat update(@PathVariable Long id, @Valid @RequestBody Chat chat) {
        Chat existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Chat not found"));
        existing.setPlannerId(chat.getPlannerId());
        existing.setCreatedAt(chat.getCreatedAt());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}