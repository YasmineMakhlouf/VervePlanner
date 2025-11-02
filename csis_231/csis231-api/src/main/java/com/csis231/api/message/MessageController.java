package com.csis231.api.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageRepository repo;
    public MessageController(MessageRepository repo) { this.repo = repo; }

    @GetMapping
    public List<Message> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@Valid @RequestBody Message message) {
        return repo.save(message);
    }

    @GetMapping("/{id}")
    public Message get(@PathVariable Long id) {
        return repo.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
    }

    @PutMapping("/{id}")
    public Message update(@PathVariable Long id, @Valid @RequestBody Message message) {
        Message existing = repo.findById(id).orElseThrow(() -> new RuntimeException("Message not found"));
        existing.setChatId(message.getChatId());
        existing.setSenderId(message.getSenderId());
        existing.setContent(message.getContent());
        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}