package com.csis231.api.habit;

import org.springframework.stereotype.Service;
import java.util.List;

/**
 * Service layer for Habit entity.
 *
 * Handles all business logic for habits:
 * - Retrieving all habits
 * - Retrieving a habit by ID
 * - Creating a habit
 * - Updating a habit
 * - Deleting a habit
 *
 * Interacts directly with HabitRepository.
 */
@Service
public class HabitService {

    private final HabitRepository repo;

    /**
     * Constructor-based dependency injection.
     *
     * @param repo the HabitRepository instance used for data access
     */
    public HabitService(HabitRepository repo) {
        this.repo = repo;
    }

    /**
     * Retrieves all habits from the repository.
     *
     * @return a list of all Habit objects
     */
    public List<Habit> getAllHabits() {
        return repo.findAll();
    }

    /**
     * Retrieves a habit by its ID.
     *
     * @param id the ID of the habit to retrieve
     * @return the Habit object with the given ID
     * @throws RuntimeException if the habit is not found
     */
    public Habit getHabitById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
    }

    /**
     * Creates a new habit.
     *
     * @param habit the Habit object to be created
     * @return the saved Habit object
     */
    public Habit createHabit(Habit habit) {
        return repo.save(habit);
    }

    /**
     * Updates an existing habit.
     *
     * @param id    the ID of the habit to update
     * @param habit the Habit object containing updated fields
     * @return the updated Habit object
     * @throws RuntimeException if the habit is not found
     */
    public Habit updateHabit(Long id, Habit habit) {
        Habit existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
        existing.setPlannerId(habit.getPlannerId());
        existing.setTitle(habit.getTitle());
        existing.setDescription(habit.getDescription());
        existing.setFrequency(habit.getFrequency());
        existing.setGoalCount(habit.getGoalCount());
        return repo.save(existing);
    }

    /**
     * Deletes a habit by its ID.
     *
     * @param id the ID of the habit to delete
     * @throws RuntimeException if the habit is not found
     */
    public void deleteHabit(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Habit not found");
        }
        repo.deleteById(id);
    }
}
