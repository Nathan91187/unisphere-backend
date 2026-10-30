
package com.example.unisphere.service;

import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.exception.*;
import com.example.unisphere.mapper.EventMapper;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.model.Rsvp;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.repository.EventRepository;
import com.example.unisphere.repository.RsvpRepository;
import com.example.unisphere.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final RsvpRepository rsvpRepository;

    public EventService(
            EventRepository eventRepository,
            EventMapper eventMapper,
            ClubRepository clubRepository,
            UserRepository userRepository,
            RsvpRepository rsvpRepository
    ) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.rsvpRepository = rsvpRepository;
    }

    private String getAuthenticatedUid() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof String uid)) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return uid;
    }

    private void verifyEventOwnership(Event event) {
        String authenticatedUid = getAuthenticatedUid();

        if (!authenticatedUid.equals(event.getOrganizer().getUid())) {
            throw new AccessDeniedException(
                    "You are not authorized to manage this event"
            );
        }
    }
    private void deleteEventAndRsvps(Event event) {
        List<Rsvp> rsvps =
                rsvpRepository.findByEventId(event.getId());

        rsvpRepository.deleteAll(rsvps);

        eventRepository.delete(event);
    }
    public List<EventResponse> getAllEvents() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map(eventMapper::toResponse).toList();
    }

    public EventResponse getEventById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(EventNotFoundException::new);

        return eventMapper.toResponse(event);
    }

    public List<EventResponse> searchEventsByName(String name) {
        List<Event> events =
                eventRepository.findByNameContainingIgnoreCase(name);

        return events.stream().map(eventMapper::toResponse).toList();
    }

    public List<EventResponse> getEventsByCategory(EventCategory category) {
        List<Event> events = eventRepository.findByCategory(category);
        return events.stream().map(eventMapper::toResponse).toList();
    }

    public List<EventResponse> getEventsByDate(LocalDate date) {
        List<Event> events = eventRepository.findByDate(date);
        return events.stream().map(eventMapper::toResponse).toList();
    }

    public List<EventResponse> getEventsOnOrAfterDate(LocalDate date) {
        List<Event> events =
                eventRepository.findByDateGreaterThanEqual(date);

        return events.stream().map(eventMapper::toResponse).toList();
    }

    public List<EventResponse> getEventsBetweenDates(
            LocalDate startDate,
            LocalDate endDate
    ) {
        if (startDate.isAfter(endDate)) {
            throw new InvalidEventDateRangeException();
        }

        List<Event> events =
                eventRepository.findByDateBetween(startDate, endDate);

        return events.stream().map(eventMapper::toResponse).toList();
    }

    public EventResponse createEvent(CreateEventRequest request) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new InvalidEventTimeRangeException();
        }

        String authenticatedUid = getAuthenticatedUid();

        Event event = eventMapper.toEntity(request);

        event.setOrganizer(
                userRepository.findById(authenticatedUid)
                        .orElseThrow(UserNotFoundException::new)
        );

        if (request.getClubId() != null) {
            event.setClub(
                    clubRepository.findById(request.getClubId())
                            .orElseThrow(ClubNotFoundException::new)
            );
        }

        event.setAttendeeCount(0);

        return eventMapper.toResponse(eventRepository.save(event));
    }

    public EventResponse updateEvent(
            UpdateEventRequest request,
            Long id
    ) {
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new InvalidEventTimeRangeException();
        }

        Event event = eventRepository.findById(id)
                .orElseThrow(EventNotFoundException::new);

        verifyEventOwnership(event);

        eventMapper.updateEntity(request, event);

        return eventMapper.toResponse(eventRepository.save(event));
    }

    @Transactional
    public void deleteEvent(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(EventNotFoundException::new);

        verifyEventOwnership(event);
        deleteEventAndRsvps(event);
    }
}
