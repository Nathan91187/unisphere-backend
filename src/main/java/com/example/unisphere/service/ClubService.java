
package com.example.unisphere.service;

import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.mapper.ClubMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubCategory;
import com.example.unisphere.model.ClubMemberRole;
import com.example.unisphere.model.ClubMembership;
import com.example.unisphere.repository.ClubMembershipRepository;
import com.example.unisphere.repository.ClubRepository;
import com.example.unisphere.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final ClubMembershipRepository clubMembershipRepository;
    private final UserRepository userRepository;

    public ClubService(
            ClubRepository clubRepository,
            ClubMapper clubMapper,
            ClubMembershipRepository clubMembershipRepository,
            UserRepository userRepository
    ) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.clubMembershipRepository = clubMembershipRepository;
        this.userRepository = userRepository;
    }

    private String getAuthenticatedUid() {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null
                || !authentication.isAuthenticated()
                || !(authentication.getPrincipal() instanceof String uid)) {
            throw new AccessDeniedException("User is not authenticated");
        }

        return uid;
    }

    private void verifyOrganizerRole(Long clubId) {
        String authenticatedUid = getAuthenticatedUid();

        ClubMembership membership = clubMembershipRepository
                .findByUserUidAndClubId(authenticatedUid, clubId)
                .orElseThrow(() -> new AccessDeniedException(
                        "You are not authorized to manage this club"
                ));

        if (membership.getRole() != ClubMemberRole.ORGANIZER) {
            throw new AccessDeniedException(
                    "Only club organizers can manage this club"
            );
        }
    }
    private void deleteClubAndMemberships(Club club) {
        clubMembershipRepository.deleteAllByClubId(club.getId());

        clubRepository.delete(club);
    }
    public ClubResponse getClubById(Long clubId) {
        Club club = clubRepository.findById(clubId)
                .orElseThrow(ClubNotFoundException::new);

        return clubMapper.toResponse(club);
    }

    @Transactional
    public ClubResponse createClub(CreateClubRequest createClubRequest) {
        String authenticatedUid = getAuthenticatedUid();

        var user = userRepository.findById(authenticatedUid)
                .orElseThrow(() -> new AccessDeniedException(
                        "User profile does not exist"
                ));

        Club club = clubMapper.toEntity(createClubRequest);
        Club savedClub = clubRepository.save(club);

        ClubMembership membership = new ClubMembership();
        membership.setClub(savedClub);
        membership.setUser(user);
        membership.setJoinedAt(LocalDateTime.now());
        membership.setRole(ClubMemberRole.ORGANIZER);

        clubMembershipRepository.save(membership);

        return clubMapper.toResponse(savedClub);
    }

    public List<ClubResponse> getAllClubs() {
        return clubRepository.findAll()
                .stream()
                .map(clubMapper::toResponse)
                .toList();
    }

    public List<ClubResponse> searchClubsByName(String name) {
        return clubRepository
                .findByNameContainingIgnoreCase(name)
                .stream()
                .map(clubMapper::toResponse)
                .toList();
    }

    public List<ClubResponse> filterClubsByCategory(
            ClubCategory category
    ) {
        return clubRepository
                .findByCategory(category)
                .stream()
                .map(clubMapper::toResponse)
                .toList();
    }

    public ClubResponse updateClub(
            UpdateClubRequest request,
            Long id
    ) {
        Club existingClub = clubRepository.findById(id)
                .orElseThrow(ClubNotFoundException::new);

        verifyOrganizerRole(id);

        clubMapper.updateEntity(request, existingClub);

        return clubMapper.toResponse(
                clubRepository.save(existingClub)
        );
    }

    @Transactional
    public void deleteClubById(Long id) {
        Club club = clubRepository.findById(id)
                .orElseThrow(ClubNotFoundException::new);

        verifyOrganizerRole(id);

        deleteClubAndMemberships(club);
    }
}
