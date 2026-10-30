package com.example.unisphere.repository;

import com.example.unisphere.model.Rsvp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RsvpRepository extends JpaRepository<Rsvp,Long> {
     boolean existsByUserUidAndEventId(String uid, Long eventId);
     List<Rsvp> findByEventId(Long eventId);
     Optional<Rsvp> findByUserUidAndEventId(String uid, Long eventId);
     List<Rsvp> findAllByUserUid(String uid);
}
