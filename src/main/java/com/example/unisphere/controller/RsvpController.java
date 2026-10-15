package com.example.unisphere.controller;

import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.service.RsvpService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/events")
public class RsvpController {
    private final RsvpService rsvpService;
    public RsvpController(RsvpService rsvpService){
        this.rsvpService = rsvpService;
    }
    @PostMapping("/{eventId}/rsvps")
    @ResponseStatus(HttpStatus.CREATED)
    public RsvpResponse createRsvp(
            @PathVariable Long eventId,
            @RequestParam String uid,
            @RequestParam RsvpStatus status) {
        return rsvpService.createRsvp(uid, eventId, status);
    }
    @GetMapping("/{eventId}/rsvps")
    public List<RsvpResponse> getRsvpsByEventId(@PathVariable Long eventId){
        return rsvpService.getRsvpsByEventId(eventId);
    }
    @GetMapping("/{eventId}/rsvps/{uid}")
    public RsvpResponse getRsvpByUserUidAndEventId(
            @PathVariable Long eventId,
            @PathVariable String uid) {
        return rsvpService.getRsvpByUserUidAndEventId(uid, eventId);
    }
    @PutMapping("/{eventId}/rsvps")
    public RsvpResponse updateRsvp(
            @PathVariable Long eventId,
            @RequestParam String uid,
            @RequestParam RsvpStatus status){
        return rsvpService.updateRsvp(uid,eventId,status);
    }
    @DeleteMapping("/{eventId}/rsvps/{uid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRsvp(@PathVariable Long eventId, @PathVariable String uid){
        rsvpService.deleteRsvp(uid,eventId);
    }
}

