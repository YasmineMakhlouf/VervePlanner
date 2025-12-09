package com.csis231.api.taskTag;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/task-tags")
public class TaskTagController {
    private final TaskTagService service;

    public TaskTagController(TaskTagService service) {
        this.service = service;
    }

    @GetMapping
    public List<TaskTag> all() {
        return service.getAllTaskTags();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskTag create(@Valid @RequestBody TaskTag taskTag) {
        return service.createTaskTag(taskTag);
    }

    @GetMapping("/{taskId}/{tagId}")
    public TaskTag get(@PathVariable Long taskId, @PathVariable Long tagId) {
        return service.getTaskTagById(taskId, tagId);
    }

    @PutMapping("/{taskId}/{tagId}")
    public TaskTag update(
            @PathVariable Long taskId,
            @PathVariable Long tagId,
            @Valid @RequestBody TaskTag taskTag) {
        return service.updateTaskTag(taskId, tagId, taskTag);
    }

    @DeleteMapping("/{taskId}/{tagId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long taskId, @PathVariable Long tagId) {
        service.deleteTaskTag(taskId, tagId);
    }
}
