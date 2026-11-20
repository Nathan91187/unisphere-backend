package com.example.unisphere.controller;


import com.example.unisphere.dto.club.ClubResponse;
import com.example.unisphere.dto.club.CreateClubRequest;
import com.example.unisphere.dto.club.UpdateClubRequest;
import com.example.unisphere.model.ClubCategory;
import com.example.unisphere.service.ClubService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubController {
    private final ClubService clubService;
    public ClubController(ClubService clubService){
        this.clubService = clubService;
    }
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public ClubResponse createClub( @Valid @RequestBody CreateClubRequest createClubRequest){
        return clubService.createClub(createClubRequest);
    }

    @PutMapping(
            value = "/{clubId}/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ClubResponse updateClubImage(
            @PathVariable Long clubId,
            @RequestParam("file") MultipartFile file
    ) {
        return clubService.updateClubImage(clubId, file);
    }
    @GetMapping
    public List<ClubResponse> getAllClubs(){
       return clubService.getAllClubs();
    }
    @GetMapping("/{clubId}")
    public ClubResponse getClubById(@PathVariable Long clubId){
        return clubService.getClubById(clubId);
    }
    @GetMapping("/search")
    public List<ClubResponse> searchClubsByName(@RequestParam String name){
        return clubService.searchClubsByName(name);
    }
    @GetMapping("/filter")
    public List<ClubResponse> filterClubsByCategory(@RequestParam ClubCategory category){
        return clubService.filterClubsByCategory(category);
    }
    @PutMapping("/{clubId}")
    public ClubResponse updateClub(@PathVariable Long clubId, @Valid @RequestBody UpdateClubRequest request){
        return clubService.updateClub(request,clubId);
    }
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{clubId}")
    public void deleteClub(@PathVariable Long clubId){
        clubService.deleteClubById(clubId);
    }

}
