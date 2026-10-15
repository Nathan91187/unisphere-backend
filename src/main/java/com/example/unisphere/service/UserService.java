package com.example.unisphere.service;

import com.example.unisphere.dto.user.CreateUserRequest;
import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.mapper.UserMapper;
import com.example.unisphere.model.User;
import com.example.unisphere.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    public UserService(
            UserRepository userRepository,
            UserMapper userMapper
            ){
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }
    public UserResponse createUser(CreateUserRequest request){
        User user = new User();
        user.setUid(request.getUid());
        user.setEmail(request.getEmail());
        user.setDisplayName(request.getDisplayName());
        user.setProfileImageUrl(request.getProfileImageUrl());
        return userMapper.toResponse(userRepository.save(user));
    }
    public UserResponse getUserByUid(String uid){
        User user = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        return userMapper.toResponse(user);
    }
    public UserResponse updateUser(String uid,UpdateUserRequest request){
        User existingUser = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        userMapper.updateEntity(existingUser,request);
        return userMapper.toResponse(userRepository.save(existingUser));
    }
    public void deleteUser(String uid){
        User user = userRepository.findById(uid).orElseThrow(UserNotFoundException::new);
        userRepository.delete(user);
    }
}
