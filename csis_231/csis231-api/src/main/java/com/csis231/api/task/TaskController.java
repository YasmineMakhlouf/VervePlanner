package com.csis231.api.task;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller class for Task entity.
 *
 * Handles HTTP requests for CRUD operations and delegates to TaskService.
 */
@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the TaskService instance
     */
    public TaskController(TaskService service) {
        this.service = service;
    }

    /**
     * Retrieve all tasks.
     *
     * @return list of all Task objects
     */
    @GetMapping
    public List<Task> all() {
        return service.getAllTasks();
    }

    /**
     * Create a new task.
     *
     * @param task the Task object to create
     * @return the created Task object
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Task create(@Valid @RequestBody Task task) {
        return service.createTask(task);
    }

    /**
     * Retrieve a task by ID.
     *
     * @param id the ID of the task
     * @return the Task object with the given ID
     */
    @GetMapping("/{id}")
    public Task get(@PathVariable Long id) {
        return service.getTaskById(id);
    }

    /**
     * Update an existing task by ID.
     *
     * @param id   the ID of the task to update
     * @param task the Task object containing updated fields
     * @return the updated Task object
     */
    @PutMapping("/{id}")
    public Task update(@PathVariable Long id, @Valid @RequestBody Task task) {
        return service.updateTask(id, task);
    }

    /**
     * Delete a task by ID.
     *
     * @param id the ID of the task to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteTask(id);
    }
}
