
package com.example.unisphere.unit.user;

import com.example.unisphere.dto.user.CreateUserRequest;
import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.mapper.UserMapper;
import com.example.unisphere.model.User;
import com.example.unisphere.repository.UserRepository;
import com.example.unisphere.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUid("user123");
        user.setEmail("user@example.com");
        user.setDisplayName("Alex");
        user.setProfileImageUrl("https://example.com/profile.png");

        userResponse = new UserResponse();
        userResponse.setUid("user123");
        userResponse.setEmail("user@example.com");
        userResponse.setDisplayName("Alex");
        userResponse.setProfileImageUrl("https://example.com/profile.png");
    }

    @Test
    void createUserSuccess() {
        CreateUserRequest request = new CreateUserRequest();
        request.setUid("user123");
        request.setEmail("user@example.com");
        request.setDisplayName("Alex");
        request.setProfileImageUrl("https://example.com/profile.png");

        when(userRepository.save(any(User.class))).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.createUser(request);

        assertEquals("user123", result.getUid());
        assertEquals("user@example.com", result.getEmail());
        assertEquals("Alex", result.getDisplayName());
        verify(userRepository).save(any(User.class));
    }

    @Test
    void getUserByUidSuccess() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.getUserByUid("user123");

        assertEquals("user123", result.getUid());
        verify(userRepository).findById("user123");
    }

    @Test
    void getUserByUidThrowsWhenNotFound() {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.getUserByUid("missing")
        );

        verify(userMapper, never()).toResponse(any(User.class));
    }

    @Test
    void updateUserSuccess() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Updated Alex");
        request.setProfileImageUrl("https://example.com/updated.png");

        when(userRepository.findById("user123")).thenReturn(Optional.of(user));
        when(userRepository.save(user)).thenReturn(user);
        when(userMapper.toResponse(user)).thenReturn(userResponse);

        UserResponse result = userService.updateUser("user123", request);

        verify(userMapper).updateEntity(user, request);
        verify(userRepository).save(user);
        assertNotNull(result);
    }

    @Test
    void updateUserThrowsWhenNotFound() {
        UpdateUserRequest request = new UpdateUserRequest();
        request.setDisplayName("Updated Alex");

        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        assertThrows(
                UserNotFoundException.class,
                () -> userService.updateUser("missing", request)
        );

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void deleteUserSuccess() {
        when(userRepository.findById("user123")).thenReturn(Optional.of(user));

        userService.deleteUser("user123");

        verify(userRepository).delete(user);
    }
}
