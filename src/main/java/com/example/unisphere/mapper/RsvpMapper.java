package com.example.unisphere.mapper;

import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.model.Rsvp;
import org.springframework.stereotype.Component;

@Component
public class RsvpMapper {
    public RsvpResponse toResponse(Rsvp rsvp){
        RsvpResponse response = new RsvpResponse();
        response.setId(rsvp.getId());
        response.setCreatedAt(rsvp.getCreatedAt());
        response.setStatus(rsvp.getStatus());
        response.setEventId(rsvp.getEvent().getId());
        response.setUserUid(rsvp.getUser().getUid());
        return response;
    }
}
