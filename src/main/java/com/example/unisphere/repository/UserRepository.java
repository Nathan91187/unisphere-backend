package com.example.unisphere.repository;

import com.example.unisphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User,String> {
    boolean existsByUsernameIgnoreCase(String username);
    // Does a user exist whose username matches this username (ignoring case),
    // AND whose UID is different from the supplied UID?
    boolean existsByUsernameIgnoreCaseAndUidNot(String username, String uid);
    Optional<User> findByUsernameIgnoreCase(String username);
    List<User> findByUsernameContainingIgnoreCase(String username);
}
