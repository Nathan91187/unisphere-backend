package com.example.unisphere.repository;

import com.example.unisphere.model.ClubMembership;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMembershipRepository extends JpaRepository<ClubMembership,Long> {
    boolean existsByUserUidAndClubId(String uid, Long clubId);
    Optional<ClubMembership> findByUserUidAndClubId(String uid, Long clubId);
    List<ClubMembership> findAllByClubId(Long clubId);
}
