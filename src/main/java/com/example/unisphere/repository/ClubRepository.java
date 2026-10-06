package com.example.unisphere.repository;

import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubRepository extends JpaRepository<Club,Long> {
List<Club> findByNameContainingIgnoreCase(String name);
List<Club> findByCategory(ClubCategory category);
}
