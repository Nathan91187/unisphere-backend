
package com.example.unisphere.unit.user;

import com.example.unisphere.controller.UserController;
import com.example.unisphere.dto.user.CreateUserRequest;
import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private UserResponse userResponse;

    @BeforeEach
    void setUp() {
        userResponse = new UserResponse();
    }

    @Test
    void createUserDelegatesToService() {
        CreateUserRequest request = new CreateUserRequest();
        when(userService.createUser(request)).thenReturn(userResponse);

        UserResponse result = userController.createUser(request);

        assertSame(userResponse, result);
        verify(userService).createUser(request);
    }

    @Test
    void getUserByUidDelegatesToService() {
        when(userService.getUserByUid("user123")).thenReturn(userResponse);

        UserResponse result = userController.getUserByUid("user123");

        assertSame(userResponse, result);
        verify(userService).getUserByUid("user123");
    }

    @Test
    void updateUserDelegatesToService() {
        UpdateUserRequest request = new UpdateUserRequest();
        when(userService.updateUser("user123", request)).thenReturn(userResponse);

        UserResponse result = userController.updateUser("user123", request);

        assertSame(userResponse, result);
        verify(userService).updateUser("user123", request);
    }

    @Test
    void deleteUserDelegatesToService() {
        userController.deleteUser("user123");

        verify(userService).deleteUser("user123");
    }
}
