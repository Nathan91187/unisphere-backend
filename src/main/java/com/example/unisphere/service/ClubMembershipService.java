package com.example.unisphere.service;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.exception.ClubMembershipAlreadyExistsException;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.mapper.ClubMembershipMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubMemberRole;
import com.example.unisphere.model.ClubMembership;
import com.example.unisphere.model.User;
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
import java.util.Optional;

@Service
public class ClubMembershipService {

    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubMembershipMapper clubMembershipMapper;

    public ClubMembershipService(
            ClubMembershipRepository clubMembershipRepository,
            ClubRepository clubRepository,
            UserRepository userRepository,
            ClubMembershipMapper clubMembershipMapper
    ) {
        this.clubMembershipRepository = clubMembershipRepository;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.clubMembershipMapper = clubMembershipMapper;
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

    @Transactional
    public ClubMembershipResponse joinClub(Long clubId) {
        String uid = getAuthenticatedUid();

        if (clubMembershipRepository.existsByUserUidAndClubId(uid, clubId)) {
            throw new ClubMembershipAlreadyExistsException();
        }

        Club club = clubRepository.findById(clubId)
                .orElseThrow(ClubNotFoundException::new);

        User user = userRepository.findById(uid)
                .orElseThrow(() -> new AccessDeniedException(
                        "User profile does not exist"
                ));

        ClubMembership membership = new ClubMembership();
        membership.setJoinedAt(LocalDateTime.now());
        membership.setClub(club);
        membership.setRole(ClubMemberRole.MEMBER);
        membership.setUser(user);

        membership = clubMembershipRepository.save(membership);
        club.setMemberCount(club.getMemberCount() + 1);
        clubRepository.save(club);
        return clubMembershipMapper.toResponse(membership);
    }

    @Transactional
    public void leaveClub(Long clubId) {
        String uid = getAuthenticatedUid();

        Optional<ClubMembership> membership =
                clubMembershipRepository.findByUserUidAndClubId(uid, clubId);

        if (membership.isEmpty()) {
            return;
        }

        if (membership.get().getRole() == ClubMemberRole.ORGANIZER
                && isOnlyOrganizer(clubId)) {
            throw new AccessDeniedException(
                    "You are the only organizer. Deleting the club is required."
            );
        }

        clubMembershipRepository.delete(membership.get());
        Club club = membership.get().getClub();
        club.setMemberCount(Math.max(0, club.getMemberCount() - 1));
        clubRepository.save(club);
    }

    public List<ClubMembershipResponse> getClubMembers(Long clubId) {
        String uid = getAuthenticatedUid();

        if (!clubMembershipRepository.existsByUserUidAndClubId(uid, clubId)) {
            throw new AccessDeniedException(
                    "Only club members can view the member list"
            );
        }

        List<ClubMembership> memberships =
                clubMembershipRepository.findAllByClubId(clubId);

        return memberships.stream()
                .map(clubMembershipMapper::toResponse)
                .toList();
    }
    public boolean isOnlyOrganizer(Long clubId) {
        String uid = getAuthenticatedUid();

        ClubMembership membership = clubMembershipRepository
                .findByUserUidAndClubId(uid, clubId)
                .orElseThrow(() -> new AccessDeniedException(
                        "You are not a member of this club"
                ));

        long organizerCount = clubMembershipRepository
                .findAllByClubId(clubId)
                .stream()
                .filter(m -> m.getRole() == ClubMemberRole.ORGANIZER)
                .count();
        if (membership.getRole() != ClubMemberRole.ORGANIZER) {
            return false;
        }
        return organizerCount == 1;
    }
    public ClubMembershipResponse getMyMembership(Long clubId) {
        String uid = getAuthenticatedUid();

        ClubMembership membership = clubMembershipRepository
                .findByUserUidAndClubId(uid, clubId)
                .orElseThrow(() -> new AccessDeniedException(
                        "You are not a member of this club"
                ));

        return clubMembershipMapper.toResponse(membership);
    }
}
