package com.example.unisphere.dto.event;

import com.example.unisphere.model.EventCategory;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public class UpdateEventRequest {

    @NotBlank(message = "Event name is required")
    @Size(max = 100, message = "Event name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Event description is required")
    @Size(max = 2000, message = "Event description must not exceed 2000 characters")
    private String description;

    private String imageUrl;

    @NotNull(message = "Event category is required")
    private EventCategory category;

    @NotNull(message = "Event date is required")
    @FutureOrPresent(message = "Event date must be today or in the future")
    private LocalDate date;

    @NotNull(message = "Event start time is required")
    private LocalTime startTime;

    @NotNull(message = "Event end time is required")
    private LocalTime endTime;

    @NotNull(message = "Event capacity is required")
    @Positive(message = "Event capacity must be greater than zero")
    private Integer capacity;

    @NotBlank(message = "Event location is required")
    @Size(max = 255, message = "Event location must not exceed 255 characters")
    private String location;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


}

