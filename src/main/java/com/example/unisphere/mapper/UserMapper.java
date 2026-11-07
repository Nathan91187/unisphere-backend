package com.example.unisphere.mapper;

import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user){
        UserResponse response = new UserResponse();
        response.setUid(user.getUid());
        response.setUsername(user.getUsername());
        response.setDisplayName(user.getDisplayName());
        response.setProfileImageUrl(user.getProfileImageUrl());
        return  response;
    }
    public void updateEntity(User existingUser, UpdateUserRequest request){

        existingUser.setUsername(request.getUsername());
        existingUser.setDisplayName(request.getDisplayName());
        existingUser.setProfileImageUrl(request.getProfileImageUrl());
    }
}
