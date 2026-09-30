package com.example.unisphere.service;

import com.example.unisphere.dto.club_dto.ClubResponse;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.model.Club;
import com.example.unisphere.repository.ClubRepository;
import org.springframework.stereotype.Service;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    public ClubService(ClubRepository clubRepository){
        this.clubRepository = clubRepository;
    }
    public ClubResponse getClubById(Long clubId){
        Club club = clubRepository.findById(clubId).orElseThrow(()-> new ClubNotFoundException());
        ClubResponse clubResponse = new ClubResponse();
        clubResponse.setCategory(club.getCategory());
        clubResponse.setId(club.getId());
        clubResponse.setDescription(club.getDescription());
        clubResponse.setName(club.getName());
        clubResponse.setImageUrl(club.getImageUrl());
        return clubResponse;
    }
}
