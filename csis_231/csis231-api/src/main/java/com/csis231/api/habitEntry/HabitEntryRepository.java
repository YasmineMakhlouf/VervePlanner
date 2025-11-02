package com.csis231.api.habitEntry;

import org.springframework.data.jpa.repository.JpaRepository;

public interface HabitEntryRepository extends JpaRepository<HabitEntry, Long> {
}
