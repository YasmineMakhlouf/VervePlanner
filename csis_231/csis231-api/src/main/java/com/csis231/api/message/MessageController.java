package com.csis231.api.message;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
    private final MessageService service;

    public MessageController(MessageService service) {
        this.service = service;
    }

    @GetMapping
    public List<Message> all() {
        return service.getAllMessages();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Message create(@Valid @RequestBody Message message) {
        return service.createMessage(message);
    }

    @GetMapping("/{id}")
    public Message get(@PathVariable Long id) {
        return service.getMessageById(id);
    }

    @PutMapping("/{id}")
    public Message update(@PathVariable Long id, @Valid @RequestBody Message message) {
        return service.updateMessage(id, message);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteMessage(id);
    }
}
