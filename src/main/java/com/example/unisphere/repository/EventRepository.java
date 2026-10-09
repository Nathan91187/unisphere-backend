package com.example.unisphere.repository;

import com.example.unisphere.model.Event;
import com.example.unisphere.model.EventCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByNameContainingIgnoreCase(String eventName);
    List<Event> findByCategory(EventCategory eventCategory);
    List<Event> findByDate(LocalDate date);
    List<Event> findByDateGreaterThanEqual(LocalDate date);
    List<Event> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
