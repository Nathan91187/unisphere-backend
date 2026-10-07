package com.example.unisphere.dto.membership;

import com.example.unisphere.model.ClubMemberRole;

import java.time.LocalDateTime;

public class ClubMembershipResponse {

    private Long id;
    private Long clubId;
    private String userId;
    private LocalDateTime joinedAt;
    private ClubMemberRole role;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getClubId() {
        return clubId;
    }
    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }
    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public LocalDateTime getJoinedAt() {
        return joinedAt;
    }
    public void setJoinedAt(LocalDateTime joinedAt) {
        this.joinedAt = joinedAt;
    }
    public ClubMemberRole getRole() {
        return role;
    }
    public void setRole(ClubMemberRole role) {
        this.role = role;
    }
}