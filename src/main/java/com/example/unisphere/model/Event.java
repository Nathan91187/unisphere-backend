package com.example.unisphere.model;

import jakarta.persistence.*;
import org.hibernate.annotations.IdGeneratorType;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String imageUrl;
    @Enumerated(EnumType.STRING)
    private EventCategory category;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer capacity;
    private int attendeeCount;
    private String location;
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private User organizer;

    @ManyToOne
    private Club club;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String eventName) {
        this.name = eventName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String eventDescription) {
        this.description = eventDescription;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String eventImageUrl) {
        this.imageUrl = eventImageUrl;
    }
    public EventCategory getCategory() {
        return category;
    }
    public void setCategory(EventCategory eventCategory) {
        this.category = eventCategory;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getStartTime() {
        return startTime;
    }
    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
    public LocalTime getEndTime() {
        return endTime;
    }
    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }
    public Integer getCapacity() {
        return capacity;
    }
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }
    public int getAttendeeCount() {
        return attendeeCount;
    }
    public void setAttendeeCount(int attendeeCount) {
        this.attendeeCount = attendeeCount;
    }
    public String getLocation() { return location; }
    public void setLocation(String location) {
        this.location = location;
    }
    public User getOrganizer() { return organizer; }
    public void setOrganizer(User organizer) { this.organizer = organizer; }
    public Club getClub() { return club; }
    public void setClub(Club club) { this.club = club; }
}
