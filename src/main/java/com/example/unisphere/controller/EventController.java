package com.example.unisphere.controller;


import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.model.EventCategory;
import com.example.unisphere.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;
    public EventController(EventService eventService){
        this.eventService = eventService;
    }
    @GetMapping
    public List<EventResponse> getAllEvents(){
        return eventService.getAllEvents();
    }
    @GetMapping("/{eventId}")
    public EventResponse getEventById(@PathVariable Long eventId){
        return eventService.getEventById(eventId);
    }
    @GetMapping("/search")
    public List<EventResponse> searchEventsByName(@RequestParam String name){
        return eventService.searchEventsByName(name);
    }
    @GetMapping("/category")
    public List<EventResponse> getEventsByCategory(@RequestParam EventCategory category){
        return eventService.getEventsByCategory(category);
    }
    @GetMapping("/date")
    public List<EventResponse> getEventsByDate(@RequestParam LocalDate date){
        return eventService.getEventsByDate(date);
    }
    @GetMapping("/upcoming")
    public List<EventResponse> getEventsOnOrAfterDate(@RequestParam LocalDate date){
        return eventService.getEventsOnOrAfterDate(date);
    }
    @GetMapping("/date-range")
    public List<EventResponse> getEventsBetweenDates(
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate){
        return eventService.getEventsBetweenDates(startDate,endDate);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@Valid @RequestBody CreateEventRequest request){
        return eventService.createEvent(request);
    }
    @PutMapping("/{eventId}")
    public EventResponse updateEvent(@Valid @RequestBody UpdateEventRequest request, @PathVariable Long eventId){
        return eventService.updateEvent(request , eventId);
    }
    @DeleteMapping("/{eventId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvent(@PathVariable Long eventId){
        eventService.deleteEvent(eventId);
    }
}
