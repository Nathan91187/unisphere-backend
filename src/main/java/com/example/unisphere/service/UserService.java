package com.example.unisphere.service;

import com.example.unisphere.dto.user.CreateUserRequest;
import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.exception.UserAlreadyExistsException;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.mapper.UserMapper;
import com.example.unisphere.model.*;
import com.example.unisphere.repository.*;
import com.google.firebase.auth.FirebaseToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final EventRepository eventRepository;
    private final RsvpRepository rsvpRepository;
    private final ClubRepository clubRepository;
    private final ClubMembershipRepository clubMembershipRepository;
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper,
            EventRepository eventRepository,
            ClubRepository clubRepository,
            ClubMembershipRepository clubMembershipRepository,
            RsvpRepository rsvpRepository
            ){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.eventRepository = eventRepository;
        this.rsvpRepository = rsvpRepository;
        this.clubRepository = clubRepository;
        this.clubMembershipRepository = clubMembershipRepository;
    }
    public UserResponse createUser(CreateUserRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String uid = (String) authentication.getPrincipal();

        FirebaseToken firebaseToken =
                (FirebaseToken) authentication.getDetails();

        String email = firebaseToken.getEmail();

        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException(
                    "Authenticated Firebase account has no email"
            );
        }
        if (userRepository.existsById(uid)) {
            throw new UserAlreadyExistsException();
        }
        User user = new User();
        user.setUid(uid);
        user.setEmail(email);
        user.setDisplayName(request.getDisplayName());
        user.setProfileImageUrl(request.getProfileImageUrl());

        return userMapper.toResponse(userRepository.save(user));
    }
    public UserResponse getUserByUid(String uid){
        User user = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(user);
    }
    public UserResponse updateUser(UpdateUserRequest request) {
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String uid = (String) authentication.getPrincipal();

        User existingUser = userRepository.findById(uid)
                .orElseThrow(UserNotFoundException::new);

        userMapper.updateEntity(existingUser, request);

        return userMapper.toResponse(userRepository.save(existingUser));
    }
    @Transactional
    public void deleteUser() {

        // 1. Get authenticated user's UID
        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String uid = (String) authentication.getPrincipal();

        User user = userRepository.findById(uid)
                .orElseThrow(UserNotFoundException::new);

        // 2. Delete all events organized by this user
        List<Event> organizedEvents =
                eventRepository.findAllByOrganizerUid(uid);

        for (Event event : organizedEvents) {
            List<Rsvp> eventRsvps =
                    rsvpRepository.findByEventId(event.getId());

            rsvpRepository.deleteAll(eventRsvps);
            eventRepository.delete(event);
        }

        // 3. Delete the user's remaining RSVPs
        List<Rsvp> userRsvps =
                rsvpRepository.findAllByUserUid(uid);

        for (Rsvp rsvp : userRsvps) {
            if (rsvp.getStatus() == RsvpStatus.GOING) {
                Event event = rsvp.getEvent();

                if (event.getAttendeeCount() > 0) {
                    event.setAttendeeCount(event.getAttendeeCount() - 1);
                }
            }
        }

        rsvpRepository.deleteAll(userRsvps);

        // 4. Delete clubs where this user is the sole organizer
        List<ClubMembership> userMemberships =
                clubMembershipRepository.findAllByUserUid(uid);

        for (ClubMembership membership : userMemberships) {

            if (membership.getRole() == ClubMemberRole.ORGANIZER) {

                List<ClubMembership> clubMemberships =
                        clubMembershipRepository.findAllByClubId(
                                membership.getClub().getId()
                        );

                long organizerCount = clubMemberships.stream()
                        .filter(m -> m.getRole() == ClubMemberRole.ORGANIZER)
                        .count();

                if (organizerCount == 1) {
                    clubMembershipRepository.deleteAllByClubId(
                            membership.getClub().getId()
                    );

                    clubRepository.delete(membership.getClub());
                }
            }
        }

        // 5. Delete the user's remaining club memberships
        clubMembershipRepository.deleteAllByUserUid(uid);

        // 6. Delete the user profile
        userRepository.delete(user);
    }
}
