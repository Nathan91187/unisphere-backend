package com.example.unisphere.service;

import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.exception.ClubNotFoundException;
import com.example.unisphere.mapper.ClubMapper;
import com.example.unisphere.model.Club;
import com.example.unisphere.model.ClubCategory;
import com.example.unisphere.repository.ClubMembershipRepository;
import com.example.unisphere.repository.ClubRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMapper clubMapper;
    private final ClubMembershipRepository clubMembershipRepository;
    public ClubService(
            ClubRepository clubRepository,
            ClubMapper clubMapper,
            ClubMembershipRepository clubMembershipRepository
    ){
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
        this.clubMembershipRepository = clubMembershipRepository;
    }
    public ClubResponse getClubById(Long clubId){
        Club club = clubRepository.findById(clubId).orElseThrow(ClubNotFoundException::new);
        return clubMapper.toResponse(club);
    }
    public ClubResponse createClub(CreateClubRequest createClubRequest){
        Club club = clubMapper.toEntity(createClubRequest);
        return clubMapper.toResponse(clubRepository.save(club));
    }
    public List<ClubResponse> getAllClubs(){
        return clubRepository.findAll().stream().map(clubMapper::toResponse).toList();
    }
    public List<ClubResponse> searchClubsByName(String name){
        return clubRepository
                .findByNameContainingIgnoreCase(name)
                .stream().map(clubMapper::toResponse)
                .toList();
    }
    public List<ClubResponse> filterClubsByCategory(ClubCategory category){
        return clubRepository
                .findByCategory(category)
                .stream().map(clubMapper::toResponse)
                .toList();
    }
    public ClubResponse updateClub(UpdateClubRequest request, Long id){
        Club existingClub = clubRepository.findById(id).orElseThrow(ClubNotFoundException::new);
        clubMapper.updateEntity(request,existingClub);
        return clubMapper.toResponse(clubRepository.save(existingClub));
    }
    @Transactional
    public void deleteClubById(Long id){
        Club club = clubRepository.findById(id).orElseThrow(ClubNotFoundException::new);
        clubMembershipRepository.deleteAllByClubId(id);
        clubRepository.delete(club);
    }

}
