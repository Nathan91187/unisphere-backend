package com.example.unisphere.web;

import com.example.unisphere.controller.EventController;
import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.exception.EventNotFoundException;
import com.example.unisphere.exception.InvalidEventDateRangeException;
import com.example.unisphere.exception.InvalidEventTimeRangeException;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.service.EventService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EventController.class)
public class EventControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventService eventService;

    @Test
    void getAllEventsReturnsEvents() throws Exception {
        EventResponse response = new EventResponse();
        response.setId(1L);

        EventResponse response1 = new EventResponse();
        response1.setId(2L);

        when(eventService.getAllEvents())
                .thenReturn(List.of(response, response1));

        mockMvc.perform(get("/events"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(eventService).getAllEvents();
    }

    @Test
    void getEventByIdReturnsEvent() throws Exception {
        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.getEventById(1L)).thenReturn(response);

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService).getEventById(1L);
    }

    @Test
    void getEventByIdReturnsNotFound() throws Exception {
        when(eventService.getEventById(1L))
                .thenThrow(new EventNotFoundException());

        mockMvc.perform(get("/events/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Event not found"));
    }

    @Test
    void searchEventsByNameReturnsMatchingEvents() throws Exception {
        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.searchEventsByName("workshop"))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/search")
                        .param("name", "workshop"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eventService).searchEventsByName("workshop");
    }

    @Test
    void getEventsByCategoryReturnsMatchingEvents() throws Exception {
        EventCategory category = EventCategory.values()[0];

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.getEventsByCategory(category))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/category")
                        .param("category", category.name()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eventService).getEventsByCategory(category);
    }

    @Test
    void getEventsByDateReturnsMatchingEvents() throws Exception {
        LocalDate date = LocalDate.of(2027, 5, 10);

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.getEventsByDate(date))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/date")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eventService).getEventsByDate(date);
    }

    @Test
    void getEventsOnOrAfterDateReturnsMatchingEvents() throws Exception {
        LocalDate date = LocalDate.of(2027, 5, 10);

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.getEventsOnOrAfterDate(date))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/upcoming")
                        .param("date", date.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eventService).getEventsOnOrAfterDate(date);
    }

    @Test
    void getEventsBetweenDatesReturnsMatchingEvents() throws Exception {
        LocalDate startDate = LocalDate.of(2027, 5, 10);
        LocalDate endDate = LocalDate.of(2027, 5, 20);

        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.getEventsBetweenDates(startDate, endDate))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(eventService).getEventsBetweenDates(startDate, endDate);
    }

    @Test
    void getEventsBetweenDatesReturnsBadRequestForInvalidRange() throws Exception {
        LocalDate startDate = LocalDate.of(2027, 5, 20);
        LocalDate endDate = LocalDate.of(2027, 5, 10);

        when(eventService.getEventsBetweenDates(startDate, endDate))
                .thenThrow(new InvalidEventDateRangeException());

        mockMvc.perform(get("/events/date-range")
                        .param("startDate", startDate.toString())
                        .param("endDate", endDate.toString()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createEventReturnsCreatedEvent() throws Exception {
        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenAnswer(invocation -> {
                    EventResponse response = new EventResponse();
                    response.setId(1L);
                    return response;
                });

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Tech Workshop",
                                  "description": "A campus technology workshop",
                                  "imageUrl": "https://example.com/event.jpg",
                                  "category": "%s",
                                  "date": "2027-05-10",
                                  "startTime": "10:00:00",
                                  "endTime": "12:00:00",
                                  "capacity": 50,
                                  "location": "Campus Hall",
                                  "organizerUid": "user123"
                                }
                                """.formatted(EventCategory.values()[0].name())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void createEventReturnsBadRequestWhenRequiredFieldsAreMissing() throws Exception {
        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "",
                                  "description": "",
                                  "category": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(eventService);
    }

    @Test
    void createEventReturnsNotFoundWhenOrganizerDoesNotExist() throws Exception {
        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validCreateEventJson()))
                .andExpect(status().isNotFound());

        verify(eventService).createEvent(any(CreateEventRequest.class));
    }

    @Test
    void createEventReturnsNotFoundWhenClubDoesNotExist() throws Exception {
        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenThrow(new ClubNotFoundException());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Tech Workshop",
                                  "description": "A campus technology workshop",
                                  "category": "%s",
                                  "date": "2027-05-10",
                                  "startTime": "10:00:00",
                                  "endTime": "12:00:00",
                                  "capacity": 50,
                                  "location": "Campus Hall",
                                  "organizerUid": "user123",
                                  "clubId": 99
                                }
                                """.formatted(EventCategory.values()[0].name())))
                .andExpect(status().isNotFound());
    }

    @Test
    void createEventReturnsBadRequestWhenEndTimeIsInvalid() throws Exception {
        when(eventService.createEvent(any(CreateEventRequest.class)))
                .thenThrow(new InvalidEventTimeRangeException());

        mockMvc.perform(post("/events")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "name": "Tech Workshop",
                                  "description": "A campus technology workshop",
                                  "category": "%s",
                                  "date": "2027-05-10",
                                  "startTime": "12:00:00",
                                  "endTime": "10:00:00",
                                  "capacity": 50,
                                  "location": "Campus Hall",
                                  "organizerUid": "user123"
                                }
                                """.formatted(EventCategory.values()[0].name())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateEventReturnsUpdatedEvent() throws Exception {
        EventResponse response = new EventResponse();
        response.setId(1L);

        when(eventService.updateEvent(any(UpdateEventRequest.class), eq(1L)))
                .thenReturn(response);

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateEventJson()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(eventService).updateEvent(any(UpdateEventRequest.class), eq(1L));
    }

    @Test
    void updateEventReturnsNotFound() throws Exception {
        when(eventService.updateEvent(any(UpdateEventRequest.class), eq(1L)))
                .thenThrow(new EventNotFoundException());

        mockMvc.perform(put("/events/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(validUpdateEventJson()))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteEventReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNoContent());

        verify(eventService).deleteEvent(1L);
    }

    @Test
    void deleteEventReturnsNotFound() throws Exception {
        doThrow(new EventNotFoundException())
                .when(eventService).deleteEvent(1L);

        mockMvc.perform(delete("/events/1"))
                .andExpect(status().isNotFound());
    }

    private String validCreateEventJson() {
        return """
                {
                  "name": "Tech Workshop",
                  "description": "A campus technology workshop",
                  "category": "%s",
                  "date": "2027-05-10",
                  "startTime": "10:00:00",
                  "endTime": "12:00:00",
                  "capacity": 50,
                  "location": "Campus Hall",
                  "organizerUid": "user123"
                }
                """.formatted(EventCategory.values()[0].name());
    }

    private String validUpdateEventJson() {
        return """
                {
                  "name": "Updated Workshop",
                  "description": "An updated campus technology workshop",
                  "category": "%s",
                  "date": "2027-05-10",
                  "startTime": "10:00:00",
                  "endTime": "12:00:00",
                  "capacity": 60,
                  "location": "Updated Campus Hall"
                }
                """.formatted(EventCategory.values()[0].name());
    }
}
