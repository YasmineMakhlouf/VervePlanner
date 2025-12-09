package com.csis231.api.chat;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chats")
public class ChatController {
    private final ChatService service;

    public ChatController(ChatService service) {
        this.service = service;
    }

    @GetMapping
    public List<Chat> all() {
        return service.getAllChats();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Chat create(@Valid @RequestBody Chat chat) {
        return service.createChat(chat);
    }

    @GetMapping("/{id}")
    public Chat get(@PathVariable Long id) {
        return service.getChatById(id);
    }

    @PutMapping("/{id}")
    public Chat update(@PathVariable Long id, @Valid @RequestBody Chat chat) {
        return service.updateChat(id, chat);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteChat(id);
    }
}
