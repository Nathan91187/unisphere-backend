package com.example.unisphere.unit.membership;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.mapper.ClubMembershipMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubMemberRole;
import com.example.unisphere.model.ClubMembership;
import com.example.unisphere.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

public class ClubMembershipMapperTest {

    private final ClubMembershipMapper clubMembershipMapper = new ClubMembershipMapper();

    @Test
    void toResponseMapsClubMembershipToResponse() {
        Club club = new Club();
        club.setId(1L);

        User user = new User();
        user.setUid("user123");

        LocalDateTime joinedAt = LocalDateTime.now();

        ClubMembership membership = new ClubMembership();
        membership.setId(10L);
        membership.setClub(club);
        membership.setUser(user);
        membership.setRole(ClubMemberRole.MEMBER);
        membership.setJoinedAt(joinedAt);

        ClubMembershipResponse actualResponse =
                clubMembershipMapper.toResponse(membership);

        Assertions.assertEquals(10L, actualResponse.getId());
        Assertions.assertEquals(1L, actualResponse.getClubId());
        Assertions.assertEquals("user123", actualResponse.getUserId());
        Assertions.assertEquals(ClubMemberRole.MEMBER, actualResponse.getRole());
        Assertions.assertEquals(joinedAt, actualResponse.getJoinedAt());
    }
}