package com.example.unisphere.repository;

import com.example.unisphere.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
}
