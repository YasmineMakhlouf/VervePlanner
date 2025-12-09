package com.csis231.api.message;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MessageService {
    private final MessageRepository repo;

    public MessageService(MessageRepository repo) {
        this.repo = repo;
    }

    public List<Message> getAllMessages() {
        return repo.findAll();
    }

    public Message getMessageById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
    }

    public Message createMessage(Message message) {
        return repo.save(message);
    }

    public Message updateMessage(Long id, Message message) {
        Message existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Message not found"));
        existing.setChatId(message.getChatId());
        existing.setSenderId(message.getSenderId());
        existing.setContent(message.getContent());
        return repo.save(existing);
    }

    public void deleteMessage(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Message not found");
        }
        repo.deleteById(id);
    }
}
