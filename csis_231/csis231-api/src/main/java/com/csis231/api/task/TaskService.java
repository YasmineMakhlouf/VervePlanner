package com.csis231.api.task;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class for Task entity.
 *
 * Handles business logic and communicates with TaskRepository for CRUD operations.
 */
@Service
public class TaskService {

    private final TaskRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the TaskRepository instance
     */
    public TaskService(TaskRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieve all tasks from the database.
     *
     * @return a list of all Task objects
     */
    public List<Task> getAllTasks() {
        return repo.findAll();
    }

    /**
     * Retrieve a single task by its ID.
     *
     * @param id the ID of the task
     * @return the Task object with the specified ID
     * @throws RuntimeException if no task with the given ID exists
     */
    public Task getTaskById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));
    }

    /**
     * Create a new task in the database.
     *
     * @param task the Task object to be created
     * @return the saved Task object
     */
    public Task createTask(Task task) {
        return repo.save(task);
    }

    /**
     * Update an existing task by ID.
     *
     * @param id   the ID of the task to update
     * @param task the Task object containing updated fields
     * @return the updated Task object
     * @throws RuntimeException if no task with the given ID exists
     */
    public Task updateTask(Long id, Task task) {
        Task existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Task not found"));

        existing.setPlannerId(task.getPlannerId());
        existing.setTitle(task.getTitle());
        existing.setDescription(task.getDescription());
        existing.setStatus(task.getStatus());
        existing.setPriority(task.getPriority());
        existing.setDueDate(task.getDueDate());

        return repo.save(existing);
    }

    /**
     * Delete a task by its ID.
     *
     * @param id the ID of the task to delete
     * @throws RuntimeException if no task with the given ID exists
     */
    public void deleteTask(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Task not found");
        }
        repo.deleteById(id);
    }
}
