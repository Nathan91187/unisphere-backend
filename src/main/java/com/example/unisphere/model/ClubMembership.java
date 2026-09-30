package com.example.unisphere.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "club_memberships",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"club_id","user_id"} )
        }
)
public class ClubMembership {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "club_id", nullable = false)
    private Club club;
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    private LocalDateTime joinedAt;
    @Enumerated(EnumType.STRING)
    private ClubMemberRole role;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Club getClub() {
        return club;
    }
    public void setClub(Club club) {
        this.club = club;
    }
    public User getUser() {
        return user;
    }
    public void setUser(User user) {
        this.user = user;
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

