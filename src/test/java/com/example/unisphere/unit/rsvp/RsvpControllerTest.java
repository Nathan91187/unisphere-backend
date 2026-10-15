
package com.example.unisphere.unit.rsvp;

import com.example.unisphere.controller.RsvpController;
import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.service.RsvpService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RsvpControllerTest {

    private RsvpController rsvpController;

    @Mock
    private RsvpService rsvpService;

    @BeforeEach
    void setUp() {
        rsvpController = new RsvpController(rsvpService);
    }

    @Test
    void createRsvpReturnsResponse() {
        RsvpResponse response = new RsvpResponse();
        when(rsvpService.createRsvp("user123", 1L, RsvpStatus.GOING))
                .thenReturn(response);

        RsvpResponse result =
                rsvpController.createRsvp(1L, "user123", RsvpStatus.GOING);

        assertSame(response, result);
        verify(rsvpService).createRsvp("user123", 1L, RsvpStatus.GOING);
    }

    @Test
    void getRsvpsByEventIdReturnsResponses() {
        List<RsvpResponse> responses = List.of(new RsvpResponse());
        when(rsvpService.getRsvpsByEventId(1L)).thenReturn(responses);

        List<RsvpResponse> result = rsvpController.getRsvpsByEventId(1L);

        assertSame(responses, result);
        verify(rsvpService).getRsvpsByEventId(1L);
    }

    @Test
    void getRsvpByUserUidAndEventIdReturnsResponse() {
        RsvpResponse response = new RsvpResponse();
        when(rsvpService.getRsvpByUserUidAndEventId("user123", 1L))
                .thenReturn(response);

        RsvpResponse result =
                rsvpController.getRsvpByUserUidAndEventId(1L, "user123");

        assertSame(response, result);
        verify(rsvpService).getRsvpByUserUidAndEventId("user123", 1L);
    }

    @Test
    void updateRsvpReturnsResponse() {
        RsvpResponse response = new RsvpResponse();
        when(rsvpService.updateRsvp("user123", 1L, RsvpStatus.MAYBE))
                .thenReturn(response);

        RsvpResponse result =
                rsvpController.updateRsvp(1L, "user123", RsvpStatus.MAYBE);

        assertSame(response, result);
        verify(rsvpService).updateRsvp("user123", 1L, RsvpStatus.MAYBE);
    }

    @Test
    void deleteRsvpDelegatesToService() {
        assertDoesNotThrow(() ->
                rsvpController.deleteRsvp(1L, "user123"));

        verify(rsvpService).deleteRsvp("user123", 1L);
    }
}
