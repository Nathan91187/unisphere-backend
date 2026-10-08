package com.example.unisphere.unit.membership;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.exception.ClubMembershipAlreadyExistsException;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.mapper.ClubMembershipMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubMembership;
import com.example.unisphere.model.User;
import com.example.unisphere.repository.ClubMembershipRepository;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.repository.UserRepository;
import com.example.unisphere.service.ClubMembershipService;
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
public class ClubMembershipServiceTest {

    private ClubMembershipService clubMembershipService;

    @Mock
    private ClubMembershipRepository clubMembershipRepository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ClubMembershipMapper clubMembershipMapper;

    @BeforeEach
    void setUp() {
        clubMembershipService = new ClubMembershipService(
                clubMembershipRepository,
                clubRepository,
                userRepository,
                clubMembershipMapper
        );
    }

    @Test
    void joinClubCreatesMembership() {
        Club club = new Club();
        club.setId(1L);

        User user = new User();
        user.setUid("user123");

        ClubMembership membership = new ClubMembership();
        ClubMembershipResponse expectedResponse = new ClubMembershipResponse();

        when(clubMembershipRepository.existsByUserUidAndClubId("user123", 1L))
                .thenReturn(false);
        when(clubRepository.findById(1L))
                .thenReturn(Optional.of(club));
        when(userRepository.findById("user123"))
                .thenReturn(Optional.of(user));
        when(clubMembershipRepository.save(any(ClubMembership.class)))
                .thenReturn(membership);
        when(clubMembershipMapper.toResponse(membership))
                .thenReturn(expectedResponse);

        ClubMembershipResponse actualResponse =
                clubMembershipService.joinClub("user123", 1L);

        Assertions.assertEquals(expectedResponse, actualResponse);

        verify(clubMembershipRepository).existsByUserUidAndClubId("user123", 1L);
        verify(clubRepository).findById(1L);
        verify(userRepository).findById("user123");
        verify(clubMembershipRepository).save(any(ClubMembership.class));
        verify(clubMembershipMapper).toResponse(membership);
    }

    @Test
    void joinClubThrowsExceptionWhenAlreadyMember() {
        when(clubMembershipRepository.existsByUserUidAndClubId("user123", 1L))
                .thenReturn(true);

        Assertions.assertThrows(
                ClubMembershipAlreadyExistsException.class,
                () -> clubMembershipService.joinClub("user123", 1L)
        );
        verify(clubMembershipRepository)
                .existsByUserUidAndClubId("user123", 1L);
        verify(clubRepository, never()).findById(anyLong());
        verify(userRepository, never()).findById(anyString());
        verify(clubMembershipRepository, never()).save(any(ClubMembership.class));
        verify(clubMembershipMapper, never()).toResponse(any(ClubMembership.class));
    }

    @Test
    void joinClubThrowsExceptionWhenClubNotFound() {
        when(clubMembershipRepository.existsByUserUidAndClubId("user123", 1L)).thenReturn(false);
        when(clubRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(
                ClubNotFoundException.class,
                () -> clubMembershipService.joinClub("user123", 1L)
        );
        verify(clubMembershipRepository).existsByUserUidAndClubId("user123", 1L);
        verify(clubRepository).findById(1L);
        verify(userRepository, never()).findById(anyString());
        verify(clubMembershipRepository, never()).save(any(ClubMembership.class));
    }

    @Test
    void leaveClubDeletesMembership() {
        ClubMembership membership = new ClubMembership();
        when(clubMembershipRepository.findByUserUidAndClubId("user123", 1L))
                .thenReturn(Optional.of(membership));
        clubMembershipService.leaveClub("user123", 1L);
        verify(clubMembershipRepository).findByUserUidAndClubId("user123", 1L);
        verify(clubMembershipRepository).delete(membership);
    }

    @Test
    void leaveClubDoesNothingWhenMembershipDoesNotExist() {
        when(clubMembershipRepository.findByUserUidAndClubId("user123", 1L))
                .thenReturn(Optional.empty());

        clubMembershipService.leaveClub("user123", 1L);

        verify(clubMembershipRepository)
                .findByUserUidAndClubId("user123", 1L);
        verify(clubMembershipRepository, never()).delete(any(ClubMembership.class));
    }

    @Test
    void getClubMembersReturnsMembershipResponses() {
        ClubMembership membership = new ClubMembership();
        ClubMembership membership1 = new ClubMembership();

        ClubMembershipResponse response = new ClubMembershipResponse();
        ClubMembershipResponse response1 = new ClubMembershipResponse();

        when(clubMembershipRepository.findAllByClubId(1L))
                .thenReturn(List.of(membership, membership1));
        when(clubMembershipMapper.toResponse(membership))
                .thenReturn(response);
        when(clubMembershipMapper.toResponse(membership1))
                .thenReturn(response1);

        List<ClubMembershipResponse> actualResponses =
                clubMembershipService.getClubMembers(1L);

        Assertions.assertEquals(2, actualResponses.size());
        Assertions.assertEquals(response, actualResponses.get(0));
        Assertions.assertEquals(response1, actualResponses.get(1));

        verify(clubMembershipRepository).findAllByClubId(1L);
        verify(clubMembershipMapper).toResponse(membership);
        verify(clubMembershipMapper).toResponse(membership1);
    }
}