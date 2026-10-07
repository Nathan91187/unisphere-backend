package com.example.unisphere.mapper;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.model.ClubMembership;
import org.springframework.stereotype.Component;

@Component
public class ClubMembershipMapper {
    public ClubMembershipResponse toResponse(ClubMembership clubMembership){
        ClubMembershipResponse clubMembershipResponse = new ClubMembershipResponse();
        clubMembershipResponse.setClubId(clubMembership.getClub().getId());
        clubMembershipResponse.setRole(clubMembership.getRole());
        clubMembershipResponse.setId(clubMembership.getId());
        clubMembershipResponse.setJoinedAt(clubMembership.getJoinedAt());
        clubMembershipResponse.setUserId(clubMembership.getUser().getUid());
        return clubMembershipResponse;
    }
}
