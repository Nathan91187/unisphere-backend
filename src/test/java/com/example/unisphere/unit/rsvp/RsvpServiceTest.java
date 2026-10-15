
package com.example.unisphere.unit.rsvp;

import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.exception.*;
import com.example.unisphere.mapper.RsvpMapper;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.Rsvp;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.model.User;
import com.example.unisphere.repository.EventRepository;
import com.example.unisphere.repository.RsvpRepository;
import com.example.unisphere.repository.UserRepository;
import com.example.unisphere.service.RsvpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RsvpServiceTest {

    private RsvpService rsvpService;

    @Mock
    private RsvpRepository rsvpRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private RsvpMapper rsvpMapper;

    @BeforeEach
    void setUp() {
        rsvpService = new RsvpService(
                rsvpRepository, userRepository, eventRepository, rsvpMapper);
    }

    private Event createEvent(int attendeeCount, int capacity) {
        Event event = new Event();
        event.setAttendeeCount(attendeeCount);
        event.setCapacity(capacity);
        return event;
    }

    private Rsvp createRsvp(Event event, RsvpStatus status) {
        Rsvp rsvp = new Rsvp();
        rsvp.setEvent(event);
        rsvp.setStatus(status);
        return rsvp;
    }

    @Test
    void createRsvpSuccess() {
        Event event = createEvent(2, 10);
        User user = new User();
        RsvpResponse response = new RsvpResponse();

        when(rsvpRepository.existsByUserUidAndEventId("uid1", 1L))
                .thenReturn(false);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(userRepository.findById("uid1")).thenReturn(Optional.of(user));
        when(rsvpRepository.save(any(Rsvp.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));
        when(rsvpMapper.toResponse(any(Rsvp.class))).thenReturn(response);

        RsvpResponse result =
                rsvpService.createRsvp("uid1", 1L, RsvpStatus.GOING);

        assertSame(response, result);
        assertEquals(3, event.getAttendeeCount());
        verify(rsvpRepository).save(any(Rsvp.class));
    }

    @Test
    void createRsvpDuplicateThrowsException() {
        when(rsvpRepository.existsByUserUidAndEventId("uid1", 1L))
                .thenReturn(true);

        assertThrows(RsvpAlreadyExistsException.class,
                () -> rsvpService.createRsvp("uid1", 1L, RsvpStatus.GOING));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void createRsvpAtCapacityThrowsException() {
        Event event = createEvent(10, 10);

        when(rsvpRepository.existsByUserUidAndEventId("uid1", 1L))
                .thenReturn(false);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(EventCapacityReachedException.class,
                () -> rsvpService.createRsvp("uid1", 1L, RsvpStatus.GOING));

        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void getRsvpsByEventIdReturnsMappedResponses() {
        Event event = createEvent(1, 10);
        Rsvp rsvp = createRsvp(event, RsvpStatus.GOING);
        RsvpResponse response = new RsvpResponse();

        when(rsvpRepository.findByEventId(1L)).thenReturn(List.of(rsvp));
        when(rsvpMapper.toResponse(rsvp)).thenReturn(response);

        List<RsvpResponse> result = rsvpService.getRsvpsByEventId(1L);

        assertEquals(1, result.size());
        assertSame(response, result.get(0));
    }

    @Test
    void getRsvpsByEventIdReturnsEmptyListWhenNoMatches() {
        when(rsvpRepository.findByEventId(1L)).thenReturn(List.of());

        assertTrue(rsvpService.getRsvpsByEventId(1L).isEmpty());
    }

    @Test
    void getRsvpByUserUidAndEventIdThrowsWhenNotFound() {
        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.empty());

        assertThrows(RsvpNotFoundException.class,
                () -> rsvpService.getRsvpByUserUidAndEventId("uid1", 1L));
    }

    @Test
    void updateRsvpToGoingIncrementsAttendeeCount() {
        Event event = createEvent(2, 10);
        Rsvp rsvp = createRsvp(event, RsvpStatus.MAYBE);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(new RsvpResponse());

        rsvpService.updateRsvp("uid1", 1L, RsvpStatus.GOING);

        assertEquals(RsvpStatus.GOING, rsvp.getStatus());
        assertEquals(3, event.getAttendeeCount());
    }

    @Test
    void updateRsvpFromGoingDecrementsAttendeeCount() {
        Event event = createEvent(2, 10);
        Rsvp rsvp = createRsvp(event, RsvpStatus.GOING);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(new RsvpResponse());

        rsvpService.updateRsvp("uid1", 1L, RsvpStatus.MAYBE);

        assertEquals(RsvpStatus.MAYBE, rsvp.getStatus());
        assertEquals(1, event.getAttendeeCount());
    }

    @Test
    void updateRsvpBetweenMaybeAndNotGoingDoesNotChangeCount() {
        Event event = createEvent(2, 10);
        Rsvp rsvp = createRsvp(event, RsvpStatus.MAYBE);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));
        when(rsvpRepository.save(rsvp)).thenReturn(rsvp);
        when(rsvpMapper.toResponse(rsvp)).thenReturn(new RsvpResponse());

        rsvpService.updateRsvp("uid1", 1L, RsvpStatus.NOT_GOING);

        assertEquals(RsvpStatus.NOT_GOING, rsvp.getStatus());
        assertEquals(2, event.getAttendeeCount());
    }

    @Test
    void updateRsvpAtCapacityThrowsException() {
        Event event = createEvent(10, 10);
        Rsvp rsvp = createRsvp(event, RsvpStatus.MAYBE);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        assertThrows(EventCapacityReachedException.class,
                () -> rsvpService.updateRsvp("uid1", 1L, RsvpStatus.GOING));

        assertEquals(RsvpStatus.MAYBE, rsvp.getStatus());
        assertEquals(10, event.getAttendeeCount());
        verify(rsvpRepository, never()).save(any());
    }

    @Test
    void deleteRsvpGoingDecrementsCountAndDeletes() {
        Event event = createEvent(2, 10);
        event.setId(1L);
        Rsvp rsvp = createRsvp(event, RsvpStatus.GOING);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        rsvpService.deleteRsvp("uid1", 1L);

        assertEquals(1, event.getAttendeeCount());
        verify(rsvpRepository).delete(rsvp);
    }

    @Test
    void deleteRsvpNotGoingDoesNotChangeCount() {
        Event event = createEvent(2, 10);
        event.setId(1L);
        Rsvp rsvp = createRsvp(event, RsvpStatus.NOT_GOING);

        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.of(rsvp));
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        rsvpService.deleteRsvp("uid1", 1L);

        assertEquals(2, event.getAttendeeCount());
        verify(rsvpRepository).delete(rsvp);
    }

    @Test
    void deleteRsvpThrowsWhenNotFound() {
        when(rsvpRepository.findByUserUidAndEventId("uid1", 1L))
                .thenReturn(Optional.empty());

        assertThrows(RsvpNotFoundException.class,
                () -> rsvpService.deleteRsvp("uid1", 1L));

        verify(rsvpRepository, never()).delete(any());
    }
}
