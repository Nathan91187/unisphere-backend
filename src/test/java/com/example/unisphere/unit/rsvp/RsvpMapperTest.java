package com.example.unisphere.unit.rsvp;

import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.mapper.RsvpMapper;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.Rsvp;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.model.User;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class RsvpMapperTest {

    private final RsvpMapper rsvpMapper = new RsvpMapper();

    @Test
    void toResponseMapsRsvpFields() {
        LocalDateTime createdAt = LocalDateTime.now();

        User user = new User();
        user.setUid("user123");

        Event event = new Event();
        event.setId(1L);

        Rsvp rsvp = new Rsvp();
        rsvp.setId(10L);
        rsvp.setCreatedAt(createdAt);
        rsvp.setStatus(RsvpStatus.GOING);
        rsvp.setEvent(event);
        rsvp.setUser(user);

        RsvpResponse response = rsvpMapper.toResponse(rsvp);

        assertEquals(10L, response.getId());
        assertEquals(createdAt, response.getCreatedAt());
        assertEquals(RsvpStatus.GOING, response.getStatus());
        assertEquals(1L, response.getEventId());
        assertEquals("user123", response.getUserUid());
    }
}