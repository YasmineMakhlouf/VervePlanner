package com.csis231.api.tag;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Tag entity.
 *
 * Handles business logic and interacts with the TagRepository for CRUD operations.
 */
@Service
public class TagService {

    private final TagRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the TagRepository instance
     */
    public TagService(TagRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all tags.
     *
     * @return list of all Tag objects
     */
    public List<Tag> getAllTags() {
        return repo.findAll();
    }

    /**
     * Retrieve a tag by ID.
     *
     * @param id the ID of the tag
     * @return the Tag object with the given ID
     * @throws RuntimeException if the tag is not found
     */
    public Tag getTagById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));
    }

    /**
     * Create a new tag.
     *
     * @param tag the Tag object to create
     * @return the created Tag object
     */
    public Tag createTag(Tag tag) {
        return repo.save(tag);
    }

    /**
     * Update an existing tag by ID.
     *
     * @param id  the ID of the tag to update
     * @param tag the Tag object containing updated fields
     * @return the updated Tag object
     * @throws RuntimeException if the tag is not found
     */
    public Tag updateTag(Long id, Tag tag) {
        Tag existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Tag not found"));

        existing.setPlannerId(tag.getPlannerId());
        existing.setName(tag.getName());

        return repo.save(existing);
    }

    /**
     * Delete a tag by ID.
     *
     * @param id the ID of the tag to delete
     * @throws RuntimeException if the tag is not found
     */
    public void deleteTag(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Tag not found");
        }
        repo.deleteById(id);
    }
}
