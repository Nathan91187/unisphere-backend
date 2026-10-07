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
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClubMembershipService {
    private final ClubMembershipRepository clubMembershipRepository;
    private final ClubRepository clubRepository;
    private final UserRepository userRepository;
    private final ClubMembershipMapper clubMembershipMapper;
    public ClubMembershipService(ClubMembershipRepository clubMembershipRepository,
                                 ClubRepository clubRepository,
                                 UserRepository userRepository,
                                 ClubMembershipMapper clubMembershipMapper
                                 ){
        this.clubMembershipRepository = clubMembershipRepository;
        this.clubRepository = clubRepository;
        this.userRepository = userRepository;
        this.clubMembershipMapper = clubMembershipMapper;
    }

    public ClubMembershipResponse joinClub(String uid, Long clubId) {
        if(clubMembershipRepository.existsByUserUidAndClubId(uid,clubId)){
            throw new ClubMembershipAlreadyExistsException();
        }
        ClubMembership membership = new ClubMembership();
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        User user = userRepository.findById(uid).orElseThrow();
        membership.setJoinedAt(LocalDateTime.now());
        membership.setClub(club);
        membership.setRole(ClubMemberRole.MEMBER);
        membership.setUser(user);
        membership = clubMembershipRepository.save(membership);
        return clubMembershipMapper.toResponse(membership);
    }
    public void leaveClub(String uid, Long clubId){
        Optional<ClubMembership> membership = clubMembershipRepository.findByUserUidAndClubId(uid,clubId);
        membership.ifPresent(clubMembershipRepository::delete);
    }
    public List<ClubMembershipResponse> getClubMembers(Long clubId){
        List<ClubMembership> membership = clubMembershipRepository.findAllByClubId(clubId);
            return membership.stream().map(clubMembershipMapper::toResponse).toList();
    }
}
