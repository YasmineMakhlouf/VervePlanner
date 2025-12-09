package com.csis231.api.habit;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * REST controller for managing habits.
 *
 * Exposes CRUD operations:
 * - GET    /api/habits        → get all habits
 * - POST   /api/habits        → create habit
 * - GET    /api/habits/{id}   → get specific habit
 * - PUT    /api/habits/{id}   → update habit
 * - DELETE /api/habits/{id}   → delete habit
 *
 * Validation:
 * - @Valid triggers validation rules defined inside the Habit entity.
 *
 * Service Layer:
 * - All business logic is delegated to HabitService.
 */
@RestController
@RequestMapping("/api/habits")
public class HabitController {

    private final HabitService service;

    /**
     * Constructor-based dependency injection.
     *
     * @param service the HabitService instance used to handle business logic
     */
    public HabitController(HabitService service) {
        this.service = service;
    }

    /**
     * Retrieves all habits.
     *
     * @return a list of all Habit records in the system
     */
    @GetMapping
    public List<Habit> all() {
        return service.getAllHabits();
    }

    /**
     * Creates a new habit.
     *
     * @param habit the Habit object to be created (validated)
     * @return the created Habit
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Habit create(@Valid @RequestBody Habit habit) {
        return service.createHabit(habit);
    }

    /**
     * Retrieves a habit by its ID.
     *
     * @param id the habit ID
     * @return the Habit with the given ID
     */
    @GetMapping("/{id}")
    public Habit get(@PathVariable Long id) {
        return service.getHabitById(id);
    }

    /**
     * Updates an existing habit.
     *
     * @param id    the ID of the habit to update
     * @param habit the new habit data (validated)
     * @return the updated Habit object
     */
    @PutMapping("/{id}")
    public Habit update(@PathVariable Long id, @Valid @RequestBody Habit habit) {
        return service.updateHabit(id, habit);
    }

    /**
     * Deletes a habit by ID.
     *
     * @param id the habit ID to delete
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.deleteHabit(id);
    }
}
