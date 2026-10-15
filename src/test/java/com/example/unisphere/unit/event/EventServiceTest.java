
package com.example.unisphere.unit.event;

import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.exception.EventNotFoundException;
import com.example.unisphere.exception.InvalidEventDateRangeException;
import com.example.unisphere.exception.InvalidEventTimeRangeException;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.mapper.EventMapper;
import com.example.unisphere.model.*;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.repository.EventRepository;
import com.example.unisphere.repository.RsvpRepository;
import com.example.unisphere.repository.UserRepository;
import com.example.unisphere.service.EventService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private EventService eventService;

    @Mock
    private EventMapper eventMapper;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RsvpRepository rsvpRepository;
    @BeforeEach
    void setUp() {
        eventService = new EventService(
                eventRepository,
                eventMapper,
                clubRepository,
                userRepository,
                rsvpRepository
        );
    }
    @Test
    void getAllEventsReturnsEventResponses() {
        Event event = new Event();
        Event event1 = new Event();
        EventResponse response = new EventResponse();
        EventResponse response1 = new EventResponse();
        when(eventRepository.findAll()).thenReturn(List.of(event, event1));
        when(eventMapper.toResponse(event)).thenReturn(response);
        when(eventMapper.toResponse(event1)).thenReturn(response1);
        List<EventResponse> actualResponses = eventService.getAllEvents();
        assertEquals(2, actualResponses.size());
        verify(eventRepository).findAll();
        verify(eventMapper).toResponse(event);
        verify(eventMapper).toResponse(event1);
    }
    @Test
    void getEventByIdReturnsEventResponse() {
        Event event = new Event();
        EventResponse response = new EventResponse();
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);
        EventResponse actualResponse = eventService.getEventById(1L);
        assertSame(response, actualResponse);
        verify(eventRepository).findById(1L);
        verify(eventMapper).toResponse(event);
    }

    @Test
    void getEventByIdThrowsWhenEventDoesNotExist() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(EventNotFoundException.class, () -> eventService.getEventById(1L));
        verify(eventRepository).findById(1L);
        verify(eventMapper, never()).toResponse(any(Event.class));
    }
    @Test
    void searchEventsByNameReturnEventResponses() {
        Event event = new Event();
        EventResponse response = new EventResponse();
        when(eventRepository.findByNameContainingIgnoreCase("workshop"))
                .thenReturn(List.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);
        List<EventResponse> actualResponses =
                eventService.searchEventsByName("workshop");
        assertEquals(1, actualResponses.size());
        verify(eventRepository).findByNameContainingIgnoreCase("workshop");
        verify(eventMapper).toResponse(event);
    }
    @Test
    void getEventsByCategoryReturnsMatchingEvents() {
        EventCategory category = EventCategory.values()[0];
        Event event = new Event();
        EventResponse response = new EventResponse();

        when(eventRepository.findByCategory(category))
                .thenReturn(List.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        List<EventResponse> actualResponses =
                eventService.getEventsByCategory(category);

        assertEquals(1, actualResponses.size());
        verify(eventRepository).findByCategory(category);
        verify(eventMapper).toResponse(event);
    }
    @Test
    void getEventsByDateReturnsMatchingEvents() {
        LocalDate date = LocalDate.of(2027, 5, 10);
        Event event = new Event();
        EventResponse response = new EventResponse();

        when(eventRepository.findByDate(date)).thenReturn(List.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        List<EventResponse> actualResponses = eventService.getEventsByDate(date);

        assertEquals(1, actualResponses.size());
        verify(eventRepository).findByDate(date);
        verify(eventMapper).toResponse(event);
    }


    @Test
    void getEventsOnOrAfterDateReturnsMatchingEvents() {
        LocalDate date = LocalDate.of(2027, 5, 10);
        Event event = new Event();
        EventResponse response = new EventResponse();

        when(eventRepository.findByDateGreaterThanEqual(date))
                .thenReturn(List.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        List<EventResponse> actualResponses =
                eventService.getEventsOnOrAfterDate(date);

        assertEquals(1, actualResponses.size());
        verify(eventRepository).findByDateGreaterThanEqual(date);
        verify(eventMapper).toResponse(event);
    }

    @Test
    void getEventsBetweenDatesReturnsMatchingEvents() {
        LocalDate startDate = LocalDate.of(2027, 5, 10);
        LocalDate endDate = LocalDate.of(2027, 5, 20);
        Event event = new Event();
        EventResponse response = new EventResponse();

        when(eventRepository.findByDateBetween(startDate, endDate))
                .thenReturn(List.of(event));
        when(eventMapper.toResponse(event)).thenReturn(response);

        List<EventResponse> actualResponses =
                eventService.getEventsBetweenDates(startDate, endDate);

        assertEquals(1, actualResponses.size());
        verify(eventRepository).findByDateBetween(startDate, endDate);
        verify(eventMapper).toResponse(event);
    }
    @Test
    void getEventsBetweenDatesThrowsWhenStartDateIsAfterEndDate() {
        LocalDate startDate = LocalDate.of(2027, 5, 20);
        LocalDate endDate = LocalDate.of(2027, 5, 10);

        assertThrows(
                InvalidEventDateRangeException.class,
                () -> eventService.getEventsBetweenDates(startDate, endDate)
        );

        verify(eventRepository, never())
                .findByDateBetween(any(LocalDate.class), any(LocalDate.class));
    }


    @Test
    void createEventReturnsResponseForStandaloneEvent() {
        CreateEventRequest request = validCreateRequest();
        Event event = new Event();
        EventResponse response = new EventResponse();
        User organizer = mock(User.class);

        when(eventMapper.toEntity(request)).thenReturn(event);
        when(userRepository.findById("organizer-1"))
                .thenReturn(Optional.of(organizer));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse actualResponse = eventService.createEvent(request);

        assertSame(response, actualResponse);
        assertEquals(0, event.getAttendeeCount());
        assertSame(organizer, event.getOrganizer());
        assertNull(event.getClub());

        verify(eventMapper).toEntity(request);
        verify(userRepository).findById("organizer-1");
        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(event);
        verifyNoInteractions(clubRepository);
    }

    @Test
    void createEventAssociatesClubWhenClubIdIsProvided() {
        CreateEventRequest request = validCreateRequest();
        request.setClubId(10L);

        Event event = new Event();
        User organizer = mock(User.class);
        Club club = mock(Club.class);
        EventResponse response = new EventResponse();

        when(eventMapper.toEntity(request)).thenReturn(event);
        when(userRepository.findById("organizer-1"))
                .thenReturn(Optional.of(organizer));
        when(clubRepository.findById(10L)).thenReturn(Optional.of(club));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse actualResponse = eventService.createEvent(request);

        assertSame(response, actualResponse);
        assertSame(organizer, event.getOrganizer());
        assertSame(club, event.getClub());
        assertEquals(0, event.getAttendeeCount());

        verify(clubRepository).findById(10L);
        verify(eventRepository).save(event);
    }

    @Test
    void createEventThrowsWhenOrganizerDoesNotExist() {
        CreateEventRequest request = validCreateRequest();

        when(eventMapper.toEntity(request)).thenReturn(new Event());
        when(userRepository.findById("organizer-1"))
                .thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> eventService.createEvent(request)
        );

        verify(eventRepository, never()).save(any(Event.class));
        verify(eventMapper, never()).toResponse(any(Event.class));
    }

    @Test
    void createEventThrowsWhenClubDoesNotExist() {
        CreateEventRequest request = validCreateRequest();
        request.setClubId(10L);

        when(eventMapper.toEntity(request)).thenReturn(new Event());
        when(userRepository.findById("organizer-1"))
                .thenReturn(Optional.of(mock(User.class)));
        when(clubRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(
                ClubNotFoundException.class,
                () -> eventService.createEvent(request)
        );

        verify(eventRepository, never()).save(any(Event.class));
        verify(eventMapper, never()).toResponse(any(Event.class));
    }

    @Test
    void createEventThrowsWhenEndTimeIsNotAfterStartTime() {
        CreateEventRequest request = validCreateRequest();
        request.setEndTime(request.getStartTime());

        assertThrows(
                InvalidEventTimeRangeException.class,
                () -> eventService.createEvent(request)
        );

        verifyNoInteractions(eventMapper);
        verifyNoInteractions(userRepository);
        verifyNoInteractions(clubRepository);
        verifyNoInteractions(eventRepository);
    }

    // ---------- UPDATE EVENT ----------

    @Test
    void updateEventReturnsUpdatedEventResponse() {
        UpdateEventRequest request = validUpdateRequest();
        Event event = new Event();
        EventResponse response = new EventResponse();

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(event)).thenReturn(event);
        when(eventMapper.toResponse(event)).thenReturn(response);

        EventResponse actualResponse = eventService.updateEvent(request, 1L);

        assertSame(response, actualResponse);
        verify(eventRepository).findById(1L);
        verify(eventMapper).updateEntity(request, event);
        verify(eventRepository).save(event);
        verify(eventMapper).toResponse(event);
    }

    @Test
    void updateEventThrowsWhenEventDoesNotExist() {
        UpdateEventRequest request = validUpdateRequest();

        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EventNotFoundException.class,
                () -> eventService.updateEvent(request, 1L)
        );

        verify(eventMapper, never()).updateEntity(any(), any());
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void updateEventThrowsWhenEndTimeIsNotAfterStartTime() {
        UpdateEventRequest request = validUpdateRequest();
        request.setEndTime(request.getStartTime());

        assertThrows(
                InvalidEventTimeRangeException.class,
                () -> eventService.updateEvent(request, 1L)
        );

        verify(eventRepository, never()).findById(any());
        verify(eventRepository, never()).save(any(Event.class));
        verifyNoInteractions(eventMapper);
    }

    @Test
    void deleteEventDeletesExistingEvent() {
        Event event = new Event();
        event.setId(1L);

        List<Rsvp> rsvps = List.of(new Rsvp(), new Rsvp());

        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(rsvpRepository.findByEventId(1L)).thenReturn(rsvps);

        assertDoesNotThrow(() -> eventService.deleteEvent(1L));

        verify(eventRepository).findById(1L);
        verify(rsvpRepository).findByEventId(1L);
        verify(rsvpRepository).deleteAll(rsvps);
        verify(eventRepository).delete(event);
    }

    @Test
    void deleteEventThrowsWhenEventDoesNotExist() {
        when(eventRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(
                EventNotFoundException.class,
                () -> eventService.deleteEvent(1L)
        );

        verify(eventRepository, never()).delete(any(Event.class));
    }


    private CreateEventRequest validCreateRequest() {
        CreateEventRequest request = new CreateEventRequest();
        request.setName("Campus Workshop");
        request.setDescription("A campus workshop");
        request.setCategory(EventCategory.values()[0]);
        request.setDate(LocalDate.now().plusDays(10));
        request.setStartTime(LocalTime.of(10, 0));
        request.setEndTime(LocalTime.of(12, 0));
        request.setCapacity(50);
        request.setLocation("Campus Hall");
        request.setOrganizerUid("organizer-1");
        return request;
    }

    private UpdateEventRequest validUpdateRequest() {
        UpdateEventRequest request = new UpdateEventRequest();
        request.setName("Updated Workshop");
        request.setDescription("An updated campus workshop");
        request.setCategory(EventCategory.values()[0]);
        request.setDate(LocalDate.now().plusDays(15));
        request.setStartTime(LocalTime.of(11, 0));
        request.setEndTime(LocalTime.of(13, 0));
        request.setCapacity(60);
        request.setLocation("Main Campus Hall");
        return request;
    }
}
