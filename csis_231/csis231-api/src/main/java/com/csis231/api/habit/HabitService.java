package com.csis231.api.habit;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitService {
    private final HabitRepository repo;

    public HabitService(HabitRepository repo) {
        this.repo = repo;
    }

    public List<Habit> getAllHabits() {
        return repo.findAll();
    }

    public Habit getHabitById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit not found"));
    }

    public Habit createHabit(Habit habit) {
        return repo.save(habit);
    }

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

    public void deleteHabit(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Habit not found");
        }
        repo.deleteById(id);
    }
}
