package com.csis231.api.taskTag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-tags")
public class TaskTagController {

    private final TaskTagRepository repo;

    public TaskTagController(TaskTagRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<TaskTag> all() {
        return repo.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskTag create(@Valid @RequestBody TaskTag taskTag) {
        return repo.save(taskTag);
    }

    @GetMapping("/{taskId}/{tagId}")
    public TaskTag get(@PathVariable Long taskId, @PathVariable Long tagId) {
        return repo.findById(new TaskTagId(taskId, tagId))
                .orElseThrow(() -> new RuntimeException("TaskTag not found"));
    }

    @PutMapping("/{taskId}/{tagId}")
    public TaskTag update(
            @PathVariable Long taskId,
            @PathVariable Long tagId,
            @Valid @RequestBody TaskTag taskTag) {
        return repo.save(taskTag);
    }

    @DeleteMapping("/{taskId}/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId, @PathVariable Long tagId) {
        repo.deleteById(new TaskTagId(taskId, tagId));
    }
}