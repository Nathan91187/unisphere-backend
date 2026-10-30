package com.example.unisphere.controller;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.service.ClubMembershipService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clubs")
public class ClubMembershipController {
    private final ClubMembershipService clubMembershipService;
    public ClubMembershipController(ClubMembershipService clubMembershipService){
        this.clubMembershipService = clubMembershipService;
    }
    @GetMapping("/{clubId}/members")
    public List<ClubMembershipResponse> getClubMembers(@PathVariable Long clubId){
        return clubMembershipService.getClubMembers(clubId);
    }
    @GetMapping("/{clubId}/memberships/leave-check")
    public boolean checkLeaveEligibility(@PathVariable Long clubId) {
        return clubMembershipService.isOnlyOrganizer(clubId);
    }
    @PostMapping("/{clubId}/memberships")
    @ResponseStatus(HttpStatus.CREATED)
    public ClubMembershipResponse joinClub(@PathVariable Long clubId){
        return clubMembershipService.joinClub(clubId);
    }
    @DeleteMapping("/{clubId}/memberships")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leaveClub(@PathVariable Long clubId){
        clubMembershipService.leaveClub(clubId);
    }


}
