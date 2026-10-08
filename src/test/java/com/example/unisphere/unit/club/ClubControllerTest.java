package com.example.unisphere.unit.club;

import com.example.unisphere.controller.ClubController;
import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.model.ClubCategory;
import com.example.unisphere.service.ClubService;
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

public class ClubControllerTest {
    private static ClubController clubController;
    @Mock
    private ClubService clubService;
    @BeforeEach
    void setUp(){
        clubController = new ClubController(clubService);
    }
    @Test
    void getClubsDelegatesToService(){
        when(clubService.getAllClubs()).thenReturn(List.of(new ClubResponse(),new ClubResponse()));
        List<ClubResponse> actualResponses = clubController.getAllClubs();
        Assertions.assertEquals(2,actualResponses.size());
        verify(clubService).getAllClubs();
    }
    @Test
    void getClubByIdDelegatesToService(){
        ClubResponse response = new ClubResponse();
        response.setId(1L);
        when(clubService.getClubById(1L)).thenReturn(response);
        ClubResponse actualResponse = clubController.getClubById(1L);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubService).getClubById(1L);
    }
    @Test
    void createClubDelegatesToService(){
        CreateClubRequest request = new CreateClubRequest();
        ClubResponse response = new ClubResponse();
        response.setId(1L);
        when(clubService.createClub(request)).thenReturn(response);
        ClubResponse actualResponse = clubController.createClub(request);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubService).createClub(request);
    }
    @Test
    void searchClubsByNameDelegatesToService(){
        when(clubService.searchClubsByName("Flutter")).thenReturn(List.of(new ClubResponse(),new ClubResponse()));
        List<ClubResponse> actualResponses = clubController.searchClubsByName("Flutter");
        Assertions.assertEquals(2,actualResponses.size());
        verify(clubService).searchClubsByName("Flutter");
    }
    @Test
    void filterClubsByCategoryDelegatesToService(){
        when(clubService.filterClubsByCategory(ClubCategory.SPORTS)).thenReturn(List.of(new ClubResponse(), new ClubResponse()));
        List<ClubResponse> actualResponses = clubController.filterClubsByCategory(ClubCategory.SPORTS);
        Assertions.assertEquals(2,actualResponses.size());
        verify(clubService).filterClubsByCategory(ClubCategory.SPORTS);
    }
    @Test
    void updateClubDelegatesToService(){
        UpdateClubRequest request = new UpdateClubRequest();
        ClubResponse response = new ClubResponse();
        response.setId(1L);
        when(clubService.updateClub(request,1L)).thenReturn(response);
        ClubResponse actualResponse = clubController.updateClub(1L, request);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubService).updateClub(request, 1L);
    }
    @Test
    void deleteClubDelegatesToService(){
        clubController.deleteClub(1L);
        verify(clubService).deleteClubById(1L);
    }
}
