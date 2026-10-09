package com.example.unisphere.service;

import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.exception.*;
import com.example.unisphere.mapper.EventMapper;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.repository.EventRepository;
import com.example.unisphere.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class EventService {
    private final EventRepository eventRepository;
    private final EventMapper eventMapper;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    public EventService(
            EventRepository eventRepository,
            EventMapper eventMapper,
            ClubRepository clubRepository,
            UserRepository userRepository
            ){
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
    }
    public List<EventResponse> getAllEvents(){
        List<Event> events = eventRepository.findAll();
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public EventResponse getEventById(Long id){
        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        return eventMapper.toResponse(event);
    }
    public List<EventResponse> searchEventsByName(String name){
        List<Event> events = eventRepository.findByNameContainingIgnoreCase(name);
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public List<EventResponse> getEventsByCategory(EventCategory category){
        List<Event> events = eventRepository.findByCategory(category);
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public List<EventResponse> getEventsByDate(LocalDate date){
        List<Event> events = eventRepository.findByDate(date);
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public List<EventResponse> getEventsOnOrAfterDate(LocalDate date){
        List<Event> events = eventRepository.findByDateGreaterThanEqual(date);
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public List<EventResponse> getEventsBetweenDates(LocalDate startDate, LocalDate endDate){
        if(startDate.isAfter(endDate)){
            throw new InvalidEventDateRangeException();
        }
        List<Event> events = eventRepository
                .findByDateBetween(startDate,endDate);
        return events.stream().map(eventMapper::toResponse).toList();
    }
    public EventResponse createEvent(CreateEventRequest request){
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new InvalidEventTimeRangeException();
        }
        Event event = eventMapper.toEntity(request);
        event.setOrganizer(
                userRepository.findById(request.getOrganizerUid()).orElseThrow(UserNotFoundException::new)
        );
        if(request.getClubId() != null){
            event.setClub(
                    clubRepository.findById(request.getClubId())
                            .orElseThrow(ClubNotFoundException::new)
            );
        }
        event.setAttendeeCount(0);
        return eventMapper.toResponse(eventRepository.save(event));
    }
    public EventResponse updateEvent(UpdateEventRequest request, Long id){
        if (!request.getEndTime().isAfter(request.getStartTime())) {
            throw new InvalidEventTimeRangeException();
        }
        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        eventMapper.updateEntity(request,event);
        return eventMapper.toResponse(eventRepository.save(event));
    }
    public void deleteEvent(Long id){
        Event event = eventRepository.findById(id).orElseThrow(EventNotFoundException::new);
        eventRepository.delete(event);
        // when event is deleted the rsvps should also be deleted, make sure of that
    }
}
