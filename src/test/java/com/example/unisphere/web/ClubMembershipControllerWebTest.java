package com.example.unisphere.web;

import com.example.unisphere.controller.ClubMembershipController;
import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.exception.ClubMembershipAlreadyExistsException;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.service.ClubMembershipService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClubMembershipController.class)
public class ClubMembershipControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ClubMembershipService clubMembershipService;

    @Test
    void getClubMembersReturnsMembers() throws Exception {
        ClubMembershipResponse response = new ClubMembershipResponse();
        response.setId(1L);

        ClubMembershipResponse response1 = new ClubMembershipResponse();
        response1.setId(2L);

        when(clubMembershipService.getClubMembers(1L))
                .thenReturn(List.of(response, response1));

        mockMvc.perform(get("/clubs/1/members"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));

        verify(clubMembershipService).getClubMembers(1L);
    }

    @Test
    void getClubMembersReturnsNotFound() throws Exception {
        when(clubMembershipService.getClubMembers(1L))
                .thenThrow(new ClubNotFoundException());

        mockMvc.perform(get("/clubs/1/members"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Club not found"));
    }

    @Test
    void joinClubReturnsMembership() throws Exception {
        ClubMembershipResponse response = new ClubMembershipResponse();
        response.setId(1L);

        when(clubMembershipService.joinClub("user123", 1L))
                .thenReturn(response);

        mockMvc.perform(post("/clubs/1/memberships")
                        .param("uid", "user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));

        verify(clubMembershipService).joinClub("user123", 1L);
    }

    @Test
    void joinClubReturnsNotFoundWhenClubDoesNotExist() throws Exception {
        when(clubMembershipService.joinClub("user123", 1L))
                .thenThrow(new ClubNotFoundException());

        mockMvc.perform(post("/clubs/1/memberships")
                        .param("uid", "user123"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Club not found"));
    }

    @Test
    void joinClubReturnsConflictWhenAlreadyMember() throws Exception {
        when(clubMembershipService.joinClub("user123", 1L))
                .thenThrow(new ClubMembershipAlreadyExistsException());

        mockMvc.perform(post("/clubs/1/memberships")
                        .param("uid", "user123"))
                .andExpect(status().isConflict());

        verify(clubMembershipService).joinClub("user123", 1L);
    }

    @Test
    void joinClubReturnsBadRequestWhenUidIsMissing() throws Exception {
        mockMvc.perform(post("/clubs/1/memberships"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clubMembershipService);
    }

    @Test
    void leaveClubReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/clubs/1/memberships")
                        .param("uid", "user123"))
                .andExpect(status().isOk());

        verify(clubMembershipService).leaveClub("user123", 1L);
    }

    @Test
    void leaveClubReturnsBadRequestWhenUidIsMissing() throws Exception {
        mockMvc.perform(delete("/clubs/1/memberships"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(clubMembershipService);
    }
}