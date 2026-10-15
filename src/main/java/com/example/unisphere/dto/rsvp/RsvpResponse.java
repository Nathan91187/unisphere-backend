package com.example.unisphere.dto.rsvp;

import com.example.unisphere.model.RsvpStatus;
import java.time.LocalDateTime;

public class RsvpResponse {

    private Long id;
    private String userUid;
    private Long eventId;
    private LocalDateTime createdAt;
    private RsvpStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public RsvpStatus getStatus() {
        return status;
    }

    public void setStatus(RsvpStatus status) {
        this.status = status;
    }
}