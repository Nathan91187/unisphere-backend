package com.example.unisphere.controller;

import com.example.unisphere.dto.membership.ClubMembershipResponse;
import com.example.unisphere.service.ClubMembershipService;
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
    @PostMapping("/{clubId}/memberships")
    public ClubMembershipResponse joinClub(@PathVariable Long clubId, @RequestParam String uid){ // provide proper uid after firebase auth and spring security
        return clubMembershipService.joinClub(uid,clubId);
    }
    @DeleteMapping("/{clubId}/memberships")
    public void leaveClub(@PathVariable Long clubId,@RequestParam String uid){
        clubMembershipService.leaveClub(uid,clubId);
    }}
