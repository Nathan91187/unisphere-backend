package com.example.unisphere.unit.membership;

import com.example.unisphere.controller.ClubMembershipController;
import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.service.ClubMembershipService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ClubMembershipControllerTest {

    private ClubMembershipController clubMembershipController;

    @Mock
    private ClubMembershipService clubMembershipService;

    @BeforeEach
    void setUp() {
        clubMembershipController = new ClubMembershipController(clubMembershipService);
    }

    @Test
    void getClubMembersDelegatesToService() {
        when(clubMembershipService.getClubMembers(1L))
                .thenReturn(List.of(new ClubMembershipResponse(), new ClubMembershipResponse()));

        List<ClubMembershipResponse> actualResponses =
                clubMembershipController.getClubMembers(1L);

        Assertions.assertEquals(2, actualResponses.size());
        verify(clubMembershipService).getClubMembers(1L);
    }

    @Test
    void joinClubDelegatesToService() {
        ClubMembershipResponse response = new ClubMembershipResponse();

        when(clubMembershipService.joinClub("user123", 1L))
                .thenReturn(response);

        ClubMembershipResponse actualResponse =
                clubMembershipController.joinClub(1L, "user123");

        Assertions.assertEquals(response, actualResponse);
        verify(clubMembershipService).joinClub("user123", 1L);
    }

    @Test
    void leaveClubDelegatesToService() {
        clubMembershipController.leaveClub(1L, "user123");

        verify(clubMembershipService).leaveClub("user123", 1L);
    }
}