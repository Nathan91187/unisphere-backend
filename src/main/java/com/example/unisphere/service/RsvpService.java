package com.example.unisphere.service;

import com.example.unisphere.dto.rsvp.RsvpResponse;
import com.example.unisphere.exception.*;
import com.example.unisphere.mapper.RsvpMapper;
import com.example.unisphere.model.Event;
import com.example.unisphere.model.Rsvp;
import com.example.unisphere.model.RsvpStatus;
import com.example.unisphere.model.User;
import com.example.unisphere.repository.EventRepository;
import com.example.unisphere.repository.RsvpRepository;
import com.example.unisphere.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RsvpService {
    private final RsvpRepository rsvprepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final RsvpMapper rsvpMapper;
    public RsvpService(
            RsvpRepository rsvprepository,
            UserRepository userRepository,
            EventRepository eventRepository,
            RsvpMapper rsvpMapper
            ){
        this.rsvprepository = rsvprepository;
        this.userRepository = userRepository;
        this.eventRepository = eventRepository;
        this.rsvpMapper = rsvpMapper;
    }
    @Transactional
    public RsvpResponse createRsvp(String uid, Long eventId, RsvpStatus status){
        if(rsvprepository.existsByUserUidAndEventId(uid,eventId)){
            throw new RsvpAlreadyExistsException();
        }
        Rsvp rsvp = new Rsvp();
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        if (status == RsvpStatus.GOING) {
            if (event.getAttendeeCount() >= event.getCapacity()) {
                throw new EventCapacityReachedException();
            }

            event.setAttendeeCount(event.getAttendeeCount() + 1);
        }
        User user = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        rsvp.setCreatedAt(LocalDateTime.now());
        rsvp.setEvent(event);
        rsvp.setUser(user);
        rsvp.setStatus(status);
        rsvp = rsvprepository.save(rsvp);
        return rsvpMapper.toResponse(rsvp);
    }
    public List<RsvpResponse> getRsvpsByEventId(Long eventId){
        return rsvprepository
                .findByEventId(eventId)
                .stream().map(rsvpMapper::toResponse)
                .toList();
    }
    public RsvpResponse getRsvpByUserUidAndEventId(String uid, Long eventId){
        return rsvpMapper
                .toResponse(rsvprepository
                .findByUserUidAndEventId(uid,eventId)
                .orElseThrow(RsvpNotFoundException::new)
        );
    }
    @Transactional
    public RsvpResponse updateRsvp(String uid, Long eventId, RsvpStatus status){
        Optional<Rsvp> rsvp = rsvprepository.findByUserUidAndEventId(uid,eventId);
        if(rsvp.isEmpty()){
            throw new RsvpNotFoundException();
        }
        Event event = eventRepository.findById(eventId).orElseThrow(EventNotFoundException::new);
        RsvpStatus currentStatus = rsvp.get().getStatus();
        if(status == RsvpStatus.GOING && currentStatus != status){
            if (event.getAttendeeCount() >= event.getCapacity()) {
                throw new EventCapacityReachedException();
            }
            rsvp.get().setStatus(status);
            event.setAttendeeCount(event.getAttendeeCount()+1);
        }
        else if((status == RsvpStatus.MAYBE || status == RsvpStatus.NOT_GOING) && currentStatus == RsvpStatus.GOING){
            rsvp.get().setStatus(status);
            if(event.getAttendeeCount() > 0){
                event.setAttendeeCount(event.getAttendeeCount()-1);
            }
        }
        else if (currentStatus != status) {
            rsvp.get().setStatus(status);
        }
        return rsvpMapper.toResponse(rsvprepository.save(rsvp.get()));
    }
    @Transactional
    public void deleteRsvp(String uid, Long eventId){
        Rsvp rsvp = rsvprepository.findByUserUidAndEventId(uid,eventId).orElseThrow(RsvpNotFoundException::new);
        Event event = eventRepository.findById(rsvp.getEvent().getId()).orElseThrow(EventNotFoundException::new);
        if (rsvp.getStatus() == RsvpStatus.GOING
                && event.getAttendeeCount() > 0) {
            event.setAttendeeCount(event.getAttendeeCount() - 1);
        }
        rsvprepository.delete(rsvp);
    }
}
