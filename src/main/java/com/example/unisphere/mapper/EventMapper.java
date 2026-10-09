package com.example.unisphere.mapper;

import com.example.unisphere.dto.event.CreateEventRequest;
import com.example.unisphere.dto.event.EventResponse;
import com.example.unisphere.dto.event.UpdateEventRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {
    public EventResponse toResponse(Event event){
        EventResponse response = new EventResponse();
        response.setCapacity(event.getCapacity());
        response.setAttendeeCount(event.getAttendeeCount());
        response.setCategory(event.getCategory());
        response.setDate(event.getDate());
        if(event.getClub() != null){
            response.setClubId(event.getClub().getId());
            response.setClubName(event.getClub().getName());
        }
        response.setDescription(event.getDescription());
        response.setId(event.getId());
        response.setLocation(event.getLocation());
        response.setImageUrl(event.getImageUrl());
        response.setEndTime(event.getEndTime());
        response.setStartTime(event.getStartTime());
        response.setOrganizerName(event.getOrganizer().getDisplayName());
        response.setOrganizerUid(event.getOrganizer().getUid());
        response.setName(event.getName());
        return response;
    }
    public Event toEntity(CreateEventRequest request){
        Event event = new Event();
        event.setName(request.getName());
        event.setCategory(request.getCategory());
        event.setDescription(request.getDescription());
        event.setCapacity(request.getCapacity());
        event.setImageUrl(request.getImageUrl());
        event.setDate(request.getDate());

        event.setStartTime(request.getStartTime());
        event.setEndTime(request.getEndTime());
        event.setLocation(request.getLocation());
        return event;
    }
    public void updateEntity(UpdateEventRequest request, Event existingEvent){
        existingEvent.setName(request.getName());
        existingEvent.setCategory(request.getCategory());
        existingEvent.setDescription(request.getDescription());
        existingEvent.setCapacity(request.getCapacity());
        existingEvent.setImageUrl(request.getImageUrl());
        existingEvent.setDate(request.getDate());

        existingEvent.setStartTime(request.getStartTime());
        existingEvent.setEndTime(request.getEndTime());
        existingEvent.setLocation(request.getLocation());
    }
}
