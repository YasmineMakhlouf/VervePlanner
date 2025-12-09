package com.csis231.api.tag;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {
    private final TagRepository repo;

    public TagService(TagRepository repo) {
        this.repo = repo;
    }

    public List<Tag> getAllTags() {
        return repo.findAll();
    }

    public Tag getTagById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    public Tag createTag(Tag tag) {
        return repo.save(tag);
    }

    public Tag updateTag(Long id, Tag tag) {
        Tag existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        existing.setPlannerId(tag.getPlannerId());
        existing.setName(tag.getName());

        return repo.save(existing);
    }

    public void deleteTag(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        repo.deleteById(id);
    }
}
