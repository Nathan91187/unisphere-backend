package com.example.unisphere.web;

import com.example.unisphere.controller.RsvpController;
import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.service.RsvpService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RsvpController.class)
class RsvpControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RsvpService rsvpService;

    @Test
    void createRsvpReturnsCreated() throws Exception {
        RsvpResponse response = new RsvpResponse();
        response.setId(1L);
        response.setEventId(10L);
        response.setUserUid("user123");
        response.setStatus(RsvpStatus.GOING);

        when(rsvpService.createRsvp("user123", 10L, RsvpStatus.GOING))
                .thenReturn(response);

        mockMvc.perform(post("/events/10/rsvps")
                        .param("uid", "user123")
                        .param("status", "GOING"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.eventId").value(10))
                .andExpect(jsonPath("$.userUid").value("user123"))
                .andExpect(jsonPath("$.status").value("GOING"));

        verify(rsvpService).createRsvp("user123", 10L, RsvpStatus.GOING);
    }

    @Test
    void getRsvpsByEventIdReturnsResponses() throws Exception {
        RsvpResponse response = new RsvpResponse();
        response.setId(1L);
        response.setEventId(10L);
        response.setUserUid("user123");
        response.setStatus(RsvpStatus.GOING);

        when(rsvpService.getRsvpsByEventId(10L))
                .thenReturn(List.of(response));

        mockMvc.perform(get("/events/10/rsvps"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].userUid").value("user123"));

        verify(rsvpService).getRsvpsByEventId(10L);
    }

    @Test
    void getRsvpByUserUidAndEventIdReturnsResponse() throws Exception {
        RsvpResponse response = new RsvpResponse();
        response.setId(1L);
        response.setEventId(10L);
        response.setUserUid("user123");
        response.setStatus(RsvpStatus.GOING);

        when(rsvpService.getRsvpByUserUidAndEventId("user123", 10L))
                .thenReturn(response);

        mockMvc.perform(get("/events/10/rsvps/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.userUid").value("user123"));

        verify(rsvpService).getRsvpByUserUidAndEventId("user123", 10L);
    }

    @Test
    void updateRsvpReturnsUpdatedResponse() throws Exception {
        RsvpResponse response = new RsvpResponse();
        response.setId(1L);
        response.setEventId(10L);
        response.setUserUid("user123");
        response.setStatus(RsvpStatus.MAYBE);

        when(rsvpService.updateRsvp("user123", 10L, RsvpStatus.MAYBE))
                .thenReturn(response);

        mockMvc.perform(put("/events/10/rsvps")
                        .param("uid", "user123")
                        .param("status", "MAYBE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("MAYBE"));

        verify(rsvpService).updateRsvp("user123", 10L, RsvpStatus.MAYBE);
    }

    @Test
    void deleteRsvpReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/events/10/rsvps/user123"))
                .andExpect(status().isNoContent());

        verify(rsvpService).deleteRsvp("user123", 10L);
    }
}