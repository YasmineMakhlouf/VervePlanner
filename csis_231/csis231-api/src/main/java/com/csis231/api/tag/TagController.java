package com.csis231.api.tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagRepository repo;

    public TagController(TagRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Tag> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@Valid @RequestBody Tag tag) {
        return repo.save(tag);
    }

    @GetMapping("/{id}")
    public Tag get(@PathVariable Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    @PutMapping("/{id}")
    public Tag update(@PathVariable Long id, @Valid @RequestBody Tag tag) {
        Tag existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        existing.setPlannerId(tag.getPlannerId());
        existing.setName(tag.getName());

        return repo.save(existing);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}