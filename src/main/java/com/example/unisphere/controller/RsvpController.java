
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

    public RsvpController(RsvpService rsvpService) {
        this.rsvpService = rsvpService;
    }

    @PostMapping("/{eventId}/rsvps")
    @ResponseStatus(HttpStatus.CREATED)
    public RsvpResponse createRsvp(
            @PathVariable Long eventId,
            @RequestParam RsvpStatus status) {
        return rsvpService.createRsvp(eventId, status);
    }

    @GetMapping("/{eventId}/rsvps")
    public List<RsvpResponse> getRsvpsByEventId(
            @PathVariable Long eventId) {
        return rsvpService.getRsvpsByEventId(eventId);
    }

    @GetMapping("/{eventId}/rsvps/me")
    public RsvpResponse getMyRsvp(
            @PathVariable Long eventId) {
        return rsvpService.getMyRsvp(eventId);
    }

    @PutMapping("/{eventId}/rsvps")
    public RsvpResponse updateRsvp(
            @PathVariable Long eventId,
            @RequestParam RsvpStatus status) {
        return rsvpService.updateRsvp(eventId, status);
    }

    @DeleteMapping("/{eventId}/rsvps")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteRsvp(@PathVariable Long eventId) {
        rsvpService.deleteRsvp(eventId);
    }
}
