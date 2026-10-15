
package com.example.unisphere.unit.event;

import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.mapper.EventMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventMapperTest {

    private EventMapper eventMapper;

    @BeforeEach
    void setUp() {
        eventMapper = new EventMapper();
    }

    @Test
    void toResponseMapsEventFields() {
        Event event = new Event();
        User organizer = mock(User.class);

        event.setId(1L);
        event.setName("Campus Workshop");
        event.setDescription("A workshop for students");
        event.setImageUrl("https://example.com/workshop.png");
        event.setCategory(EventCategory.values()[0]);
        event.setDate(LocalDate.of(2027, 5, 10));
        event.setStartTime(LocalTime.of(10, 0));
        event.setEndTime(LocalTime.of(12, 0));
        event.setCapacity(50);
        event.setAttendeeCount(20);
        event.setLocation("Campus Hall");
        event.setOrganizer(organizer);

        when(organizer.getDisplayName()).thenReturn("Alex");
        when(organizer.getUid()).thenReturn("user-1");

        EventResponse response = eventMapper.toResponse(event);

        assertEquals(event.getId(), response.getId());
        assertEquals(event.getName(), response.getName());
        assertEquals(event.getDescription(), response.getDescription());
        assertEquals(event.getImageUrl(), response.getImageUrl());
        assertEquals(event.getCategory(), response.getCategory());
        assertEquals(event.getDate(), response.getDate());
        assertEquals(event.getStartTime(), response.getStartTime());
        assertEquals(event.getEndTime(), response.getEndTime());
        assertEquals(event.getCapacity(), response.getCapacity());
        assertEquals(event.getAttendeeCount(), response.getAttendeeCount());
        assertEquals(event.getLocation(), response.getLocation());
        assertEquals("Alex", response.getOrganizerName());
        assertEquals("user-1", response.getOrganizerUid());

        assertNull(response.getClubId());
        assertNull(response.getClubName());
    }
    @Test
    void toEntityMapsCreateRequestFields() {
        CreateEventRequest request = new CreateEventRequest();

        request.setName("Campus Workshop");
        request.setDescription("A workshop for students");
        request.setImageUrl("https://example.com/workshop.png");
        request.setCategory(EventCategory.values()[0]);
        request.setDate(LocalDate.of(2027, 5, 10));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setCapacity(50);
        request.setLocation("Campus Hall");

        Event event = eventMapper.toEntity(request);

        assertEquals(request.getName(), event.getName());
        assertEquals(request.getDescription(), event.getDescription());
        assertEquals(request.getImageUrl(), event.getImageUrl());
        assertEquals(request.getCategory(), event.getCategory());
        assertEquals(request.getDate(), event.getDate());
        assertEquals(request.getStartTime(), event.getStartTime());
        assertEquals(request.getEndTime(), event.getEndTime());
        assertEquals(request.getCapacity(), event.getCapacity());
        assertEquals(request.getLocation(), event.getLocation());
        assertNull(event.getId());
        assertNull(event.getOrganizer());
        assertNull(event.getClub());
        assertEquals(0, event.getAttendeeCount());
    }

    @Test
    void updateEntityUpdatesEditableFields() {
        UpdateEventRequest request = new UpdateEventRequest();

        request.setName("Updated Workshop");
        request.setDescription("Updated description");
        request.setImageUrl("https://example.com/updated.png");
        request.setCategory(EventCategory.values()[0]);
        request.setDate(LocalDate.of(2027, 6, 15));
        request.setStartTime(LocalTime.of(11, 0));
        request.setEndTime(LocalTime.of(13, 0));
        request.setCapacity(60);
        request.setLocation("New Campus Hall");

        Event existingEvent = new Event();

        eventMapper.updateEntity(request, existingEvent);

        assertEquals(request.getName(), existingEvent.getName());
        assertEquals(request.getDescription(), existingEvent.getDescription());
        assertEquals(request.getImageUrl(), existingEvent.getImageUrl());
        assertEquals(request.getCategory(), existingEvent.getCategory());
        assertEquals(request.getDate(), existingEvent.getDate());
        assertEquals(request.getStartTime(), existingEvent.getStartTime());
        assertEquals(request.getEndTime(), existingEvent.getEndTime());
        assertEquals(request.getCapacity(), existingEvent.getCapacity());
        assertEquals(request.getLocation(), existingEvent.getLocation());
    }
}
