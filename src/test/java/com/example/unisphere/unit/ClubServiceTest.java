package com.example.unisphere.unit;

import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.mapper.ClubMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubCategory;
import com.example.unisphere.repository.ClubMembershipRepository;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.service.ClubService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {
    private ClubService clubService;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private ClubMapper clubMapper;
    @Mock
    private ClubMembershipRepository clubMembershipRepository;
    @BeforeEach
    void setUp(){
        clubService = new ClubService(clubRepository,clubMapper,clubMembershipRepository);
    }
    @Test
    void getClubByIdReturnsClubResponse(){
        Club club = new Club();
        club.setId(1L);
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(clubMapper.toResponse(club)).thenReturn(expectedResponse);
        ClubResponse actualResponse = clubService.getClubById(1L);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubRepository).findById(1L);
        verify(clubMapper).toResponse(club);
    }
    @Test
    void getClubByIdThrowsExceptionWhenClubNotFound(){
        when(clubRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ClubNotFoundException.class, ()->clubService.getClubById(1L));
        verify(clubRepository).findById(1L);
        verify(clubMapper, never()).toResponse(any());
    }
    @Test
    void getAllClubsReturnsClubResponses(){
        Club club = new Club();
        club.setId(1L);
        Club club1 = new Club();
        club1.setId(2L);
        ClubResponse response = new ClubResponse();
        response.setId(1L);
        ClubResponse response1 = new ClubResponse();
        response1.setId(2L);
        when(clubRepository.findAll()).thenReturn(List.of(club,club1));
        when(clubMapper.toResponse(club)).thenReturn(response);
        when(clubMapper.toResponse(club1)).thenReturn(response1);
        List<ClubResponse> actualResponses = clubService.getAllClubs();
        Assertions.assertEquals(2, actualResponses.size());
        Assertions.assertEquals(1L, actualResponses.get(0).getId());
        Assertions.assertEquals(2L, actualResponses.get(1).getId());
        verify(clubRepository).findAll();
        verify(clubMapper).toResponse(club);
        verify(clubMapper).toResponse(club1);
    }
    @Test
    void createClubReturnsClubResponse(){
        CreateClubRequest request = new CreateClubRequest();
        Club club = new Club();
        club.setId(1L);
        ClubResponse expectedResponse = new ClubResponse();
        expectedResponse.setId(1L);
        when(clubMapper.toEntity(request)).thenReturn(club);
        when(clubRepository.save(club)).thenReturn(club);
        when(clubMapper.toResponse(club)).thenReturn(expectedResponse);
        ClubResponse actualResponse = clubService.createClub(request);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubRepository).save(club);
        verify(clubMapper).toResponse(club);
        verify(clubMapper).toEntity(request);
    }
    @Test
    void searchClubsByName(){
        Club club = new Club();
        club.setName("Flutter group");
        Club club1 = new Club();
        club1.setName("Flutter devs");
        ClubResponse response = new ClubResponse();
        response.setName("Flutter group");
        ClubResponse response1 = new ClubResponse();
        response1.setName("Flutter devs");
        when(clubRepository.findByNameContainingIgnoreCase("Flutter")).thenReturn(List.of(club,club1));
        when(clubMapper.toResponse(club)).thenReturn(response);
        when(clubMapper.toResponse(club1)).thenReturn(response1);
        List<ClubResponse> actualResponses = clubService.searchClubsByName("Flutter");
        Assertions.assertEquals(2,actualResponses.size());
        Assertions.assertEquals("Flutter group", actualResponses.get(0).getName());
        Assertions.assertEquals("Flutter devs", actualResponses.get(1).getName());
        verify(clubRepository).findByNameContainingIgnoreCase("Flutter");
        verify(clubMapper).toResponse(club);
        verify(clubMapper).toResponse(club1);
    }
    @Test
    void filterClubsByCategory(){
        ClubCategory sports = ClubCategory.SPORTS;
        Club club = new Club();
        club.setCategory(sports);
        Club club1 = new Club();
        club1.setCategory(sports);
        ClubResponse response = new ClubResponse();
        response.setCategory(sports);
        ClubResponse response1 = new ClubResponse();
        response1.setCategory(sports);
        when(clubRepository.findByCategory(sports)).thenReturn(List.of(club,club1));
        when(clubMapper.toResponse(club)).thenReturn(response);
        when(clubMapper.toResponse(club1)).thenReturn(response1);
        List<ClubResponse> actualResponses = clubService.filterClubsByCategory(sports);
        Assertions.assertEquals(2,actualResponses.size());
        Assertions.assertEquals(sports, actualResponses.get(0).getCategory());
        Assertions.assertEquals(sports, actualResponses.get(1).getCategory());
        verify(clubRepository).findByCategory(sports);
        verify(clubMapper).toResponse(club);
        verify(clubMapper).toResponse(club1);
    }
    @Test
    void updateClubReturnsResponse(){
        UpdateClubRequest request = new UpdateClubRequest();
        ClubResponse updatedResponse = new ClubResponse();
        Club club = new Club();
        club.setId(1L);
        updatedResponse.setId(1L);
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(clubRepository.save(club)).thenReturn(club);
        when(clubMapper.toResponse(club)).thenReturn(updatedResponse);
        ClubResponse actualResponse = clubService.updateClub(request,1L);
        Assertions.assertEquals(1L, actualResponse.getId());
        verify(clubRepository).findById(1L);
        verify(clubRepository).save(club);
        verify(clubMapper).updateEntity(request,club);
        verify(clubMapper).toResponse(club);
    }
    @Test
    void updateClubThrowsExceptionWhenClubNotFound(){
        when(clubRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ClubNotFoundException.class, ()->clubService.updateClub(new UpdateClubRequest(),1L));
        verify(clubRepository).findById(1L);
        verify(clubRepository,never()).save(any(Club.class));
        verify(clubMapper,never()).updateEntity(any(UpdateClubRequest.class),any(Club.class));
        verify(clubMapper,never()).toResponse(any(Club.class));
    }
    @Test
    void deleteClubByIdDeletesClub(){
        Club club = new Club();
        club.setId(1L);
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        clubService.deleteClubById(1L);
        verify(clubRepository).findById(1L);
        verify(clubMembershipRepository).deleteAllByClubId(1L);
        verify(clubRepository).delete(club);
    }
    @Test
    void deleteClubByIdThrowsExceptionWhenClubNotFound(){
        when(clubRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(ClubNotFoundException.class, ()-> clubService.deleteClubById(1L));
        verify(clubRepository).findById(1L);
        verify(clubMembershipRepository, never()).deleteAllByClubId(anyLong());
        verify(clubRepository,never()).delete(any(Club.class));
    }
}

