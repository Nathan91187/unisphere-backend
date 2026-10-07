package com.example.unisphere.unit;

import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.mapper.ClubMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubCategory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

public class ClubMapperTest {
    private ClubMapper clubMapper;
    @BeforeEach
    void setUp(){
        clubMapper = new ClubMapper();
    }
    @Test
    void toResponseMapsClubToResponse(){
        Club club = new Club();
        club.setId(1L);
        club.setCategory(ClubCategory.SPORTS);
        club.setDescription("Test description");
        club.setImageUrl("Test image url");
        club.setName("Test name");
        club.setMemberCount(1);
        ClubResponse actualResponse = clubMapper.toResponse(club);
        Assertions.assertEquals(1L, actualResponse.getId());
        Assertions.assertEquals(ClubCategory.SPORTS, actualResponse.getCategory());
        Assertions.assertEquals("Test image url", actualResponse.getImageUrl());
        Assertions.assertEquals("Test name", actualResponse.getName());
        Assertions.assertEquals(1, actualResponse.getMemberCount());
        Assertions.assertEquals("Test description", actualResponse.getDescription());
    }
    @Test
    void toEntityMapsCreateClubRequestToClub(){
        CreateClubRequest request = new CreateClubRequest();
        request.setCategory(ClubCategory.SPORTS);
        request.setDescription("Test description");
        request.setImageUrl("Test image url");
        request.setName("Test name");
        Club club = clubMapper.toEntity(request);
        Assertions.assertEquals(ClubCategory.SPORTS, club.getCategory());
        Assertions.assertEquals("Test image url", club.getImageUrl());
        Assertions.assertEquals("Test name", club.getName());
        Assertions.assertEquals("Test description", club.getDescription());
        Assertions.assertEquals(1, club.getMemberCount());
    }
    @Test
    void updateEntityModifiesExistingClub(){
        UpdateClubRequest request = new UpdateClubRequest();
        request.setCategory(ClubCategory.SPORTS);
        request.setDescription("Test description");
        request.setImageUrl("Test image url");
        request.setName("Test name");
        Club existingClub = new Club();
        existingClub.setCategory(ClubCategory.TECHNOLOGY);
        existingClub.setDescription("Old test description");
        existingClub.setImageUrl("Old test image url");
        existingClub.setName("Old test name");
        clubMapper.updateEntity(request,existingClub);
        Assertions.assertEquals(ClubCategory.SPORTS, existingClub.getCategory());
        Assertions.assertEquals("Test image url", existingClub.getImageUrl());
        Assertions.assertEquals("Test name", existingClub.getName());
        Assertions.assertEquals("Test description", existingClub.getDescription());
    }
}
