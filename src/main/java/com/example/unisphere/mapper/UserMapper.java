package com.example.unisphere.mapper;

import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        UserResponse response = new UserResponse();
        response.setEmail(user.getEmail());
        response.setUid(user.getUid());
        response.setDisplayName(user.getDisplayName());
        response.setProfileImageUrl(user.getProfileImageUrl());
        return  response;
    }
    public void updateEntity(User existingUser, UpdateUserRequest request){
        existingUser.setDisplayName(request.getDisplayName());
        existingUser.setProfileImageUrl(request.getProfileImageUrl());
    }
}
