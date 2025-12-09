package com.csis231.api.habitEntry;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HabitEntryService {
    private final HabitEntryRepository repo;

    public HabitEntryService(HabitEntryRepository repo) {
        this.repo = repo;
    }

    public List<HabitEntry> getAllEntries() {
        return repo.findAll();
    }

    public HabitEntry getEntryById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit entry not found"));
    }

    public HabitEntry createEntry(HabitEntry habitEntry) {
        return repo.save(habitEntry);
    }

    public HabitEntry updateEntry(Long id, HabitEntry habitEntry) {
        HabitEntry existing = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Habit entry not found"));
        existing.setHabitId(habitEntry.getHabitId());
        existing.setEntryDate(habitEntry.getEntryDate());
        existing.setCompleted(habitEntry.getCompleted());
        return repo.save(existing);
    }

    public void deleteEntry(Long id) {
        if (!repo.existsById(id)) {
            throw new RuntimeException("Habit entry not found");
        }
        repo.deleteById(id);
    }
}
