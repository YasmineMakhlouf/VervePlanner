package com.csis231.api.tag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST controller for Tag entity.
 *
 * Handles HTTP requests for CRUD operations on tags.
 */
@RestController
@RequestMapping("/api/tags")
public class TagController {

    private final TagService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the TagService for business logic
     */
    public TagController(TagService service) {
        this.service = service;
    }

    /**
     * Retrieve all tags.
     *
     * @return list of all Tag objects
     */
    @GetMapping
    public List<Tag> all() {
        return service.getAllTags();
    }

    /**
     * Create a new tag.
     *
     * @param tag the Tag object to create
     * @return the created Tag object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tag create(@Valid @RequestBody Tag tag) {
        return service.createTag(tag);
    }

    /**
     * Retrieve a tag by ID.
     *
     * @param id the ID of the tag
     * @return the Tag object with the given ID
     */
    @GetMapping("/{id}")
    public Tag get(@PathVariable Long id) {
        return service.getTagById(id);
    }

    /**
     * Update an existing tag by ID.
     *
     * @param id  the ID of the tag to update
     * @param tag the Tag object containing updated fields
     * @return the updated Tag object
     */
    @PutMapping("/{id}")
    public Tag update(@PathVariable Long id, @Valid @RequestBody Tag tag) {
        return service.updateTag(id, tag);
    }

    /**
     * Delete a tag by ID.
     *
     * @param id the ID of the tag to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteTag(id);
    }
}
