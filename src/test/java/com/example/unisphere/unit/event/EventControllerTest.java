
package com.example.unisphere.unit.event;

import com.example.unisphere.controller.EventController;
import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.service.EventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventControllerTest {

    @Mock
    private EventService eventService;

    private EventController eventController;

    @BeforeEach
    void setUp() {
        eventController = new EventController(eventService);
    }

    @Test
    void getAllEventsReturnsEventResponses() {
        List<EventResponse> responses = List.of(new EventResponse());
        when(eventService.getAllEvents()).thenReturn(responses);

        List<EventResponse> actual = eventController.getAllEvents();

        assertSame(responses, actual);
        verify(eventService).getAllEvents();
    }

    @Test
    void getEventByIdReturnsEventResponse() {
        EventResponse response = new EventResponse();
        when(eventService.getEventById(1L)).thenReturn(response);

        EventResponse actual = eventController.getEventById(1L);

        assertSame(response, actual);
        verify(eventService).getEventById(1L);
    }

    @Test
    void searchEventsByNameReturnsMatchingEvents() {
        List<EventResponse> responses = List.of(new EventResponse());
        when(eventService.searchEventsByName("workshop")).thenReturn(responses);

        List<EventResponse> actual = eventController.searchEventsByName("workshop");

        assertSame(responses, actual);
        verify(eventService).searchEventsByName("workshop");
    }

    @Test
    void getEventsByCategoryReturnsMatchingEvents() {
        EventCategory category = EventCategory.values()[0];
        List<EventResponse> responses = List.of(new EventResponse());
        when(eventService.getEventsByCategory(category)).thenReturn(responses);

        List<EventResponse> actual = eventController.getEventsByCategory(category);

        assertSame(responses, actual);
        verify(eventService).getEventsByCategory(category);
    }

    @Test
    void getEventsByDateReturnsMatchingEvents() {
        LocalDate date = LocalDate.of(2027, 5, 10);
        List<EventResponse> responses = List.of(new EventResponse());
        when(eventService.getEventsByDate(date)).thenReturn(responses);

        List<EventResponse> actual = eventController.getEventsByDate(date);

        assertSame(responses, actual);
        verify(eventService).getEventsByDate(date);
    }

    @Test
    void getEventsOnOrAfterDateReturnsMatchingEvents() {
        LocalDate date = LocalDate.of(2027, 5, 10);
        List<EventResponse> responses = List.of(new EventResponse());
        when(eventService.getEventsOnOrAfterDate(date)).thenReturn(responses);

        List<EventResponse> actual = eventController.getEventsOnOrAfterDate(date);

        assertSame(responses, actual);
        verify(eventService).getEventsOnOrAfterDate(date);
    }

    @Test
    void getEventsBetweenDatesReturnsMatchingEvents() {
        LocalDate startDate = LocalDate.of(2027, 5, 10);
        LocalDate endDate = LocalDate.of(2027, 5, 20);
        List<EventResponse> responses = List.of(new EventResponse());

        when(eventService.getEventsBetweenDates(startDate, endDate))
                .thenReturn(responses);

        List<EventResponse> actual =
                eventController.getEventsBetweenDates(startDate, endDate);

        assertSame(responses, actual);
        verify(eventService).getEventsBetweenDates(startDate, endDate);
    }

    @Test
    void createEventReturnsCreatedResponse() {
        CreateEventRequest request = new CreateEventRequest();
        EventResponse response = new EventResponse();
        when(eventService.createEvent(request)).thenReturn(response);

        EventResponse actual = eventController.createEvent(request);

        assertSame(response, actual);
        verify(eventService).createEvent(request);
    }

    @Test
    void updateEventReturnsUpdatedResponse() {
        UpdateEventRequest request = new UpdateEventRequest();
        EventResponse response = new EventResponse();
        when(eventService.updateEvent(request, 1L)).thenReturn(response);

        EventResponse actual = eventController.updateEvent(request, 1L);

        assertSame(response, actual);
        verify(eventService).updateEvent(request, 1L);
    }

    @Test
    void deleteEventDelegatesToService() {
        eventController.deleteEvent(1L);

        verify(eventService).deleteEvent(1L);
    }
}
