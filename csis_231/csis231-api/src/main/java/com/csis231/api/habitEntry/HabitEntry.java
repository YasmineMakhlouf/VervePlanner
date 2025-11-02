package com.csis231.api.habitEntry;

import jakarta.persistence.*;

import java.time.Instant;
import java.time.LocalDate;

@Entity
@Table(name = "habit_entries", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"habit_id", "entry_date"})
})
public class HabitEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "habit_id", nullable = false)
    private Long habitId;

    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    private Boolean completed;

    @Column(name = "created_at")
    private Instant createdAt = Instant.now();

    public HabitEntry() {}

    public HabitEntry(Long habitId, LocalDate entryDate, Boolean completed) {
        this.habitId = habitId;
        this.entryDate = entryDate;
        this.completed = completed;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getHabitId() { return habitId; }
    public void setHabitId(Long habitId) { this.habitId = habitId; }

    public LocalDate getEntryDate() { return entryDate; }
    public void setEntryDate(LocalDate entryDate) { this.entryDate = entryDate; }

    public Boolean getCompleted() { return completed; }
    public void setCompleted(Boolean completed) { this.completed = completed; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
