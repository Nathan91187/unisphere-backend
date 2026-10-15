
package com.example.unisphere.unit.user;

import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.mapper.UserMapper;
import com.example.unisphere.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        userMapper = new UserMapper();
    }

    @Test
    void toResponseMapsAllFields() {
        User user = new User();
        user.setUid("user123");
        user.setEmail("user@example.com");
        user.setDisplayName("Alex");
        user.setProfileImageUrl("https://example.com/profile.png");

        UserResponse response = userMapper.toResponse(user);

        assertEquals(user.getUid(), response.getUid());
        assertEquals(user.getEmail(), response.getEmail());
        assertEquals(user.getDisplayName(), response.getDisplayName());
        assertEquals(user.getProfileImageUrl(), response.getProfileImageUrl());
    }

    @Test
    void updateEntityUpdatesEditableFieldsOnly() {
        User user = new User();
        user.setUid("user123");
        user.setEmail("user@example.com");
        user.setDisplayName("Alex");
        user.setProfileImageUrl("https://example.com/old.png");

        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Updated Alex");
        request.setProfileImageUrl("https://example.com/new.png");

        userMapper.updateEntity(user, request);

        assertEquals("Updated Alex", user.getDisplayName());
        assertEquals("https://example.com/new.png", user.getProfileImageUrl());

        assertEquals("user123", user.getUid());
        assertEquals("user@example.com", user.getEmail());
    }
}
