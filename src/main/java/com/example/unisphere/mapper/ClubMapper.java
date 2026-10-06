package com.example.unisphere.mapper;

import com.example.unisphere.dto.club_dto.ClubResponse;
import com.example.unisphere.dto.club_dto.CreateClubRequest;
import com.example.unisphere.dto.club_dto.UpdateClubRequest;
import com.example.unisphere.model.Club;
import org.springframework.stereotype.Component;

@Component
public class ClubMapper {
    public ClubResponse toResponse(Club club){
        ClubResponse response = new ClubResponse();
        response.setMemberCount(club.getMemberCount());
        response.setCategory(club.getCategory());
        response.setId(club.getId());
        response.setDescription(club.getDescription());
        response.setImageUrl(club.getImageUrl());
        response.setName(club.getName());
        return response;
    }
    public Club toEntity(CreateClubRequest createClubRequest){
        Club club = new Club();
        club.setCategory(createClubRequest.getCategory());
        club.setDescription(createClubRequest.getDescription());
        club.setName(createClubRequest.getName());
        club.setMemberCount(1);
        club.setImageUrl(createClubRequest.getImageUrl());
        return club;
    }
    public void updateEntity(UpdateClubRequest request, Club existingClub){
        existingClub.setImageUrl(request.getImageUrl());
        existingClub.setName(request.getName());
        existingClub.setCategory(request.getCategory());
        existingClub.setDescription(request.getDescription());
    }
}
