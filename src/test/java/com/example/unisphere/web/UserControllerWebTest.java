
package com.example.unisphere.web;

import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.exception.UserNotFoundException;
import com.example.unisphere.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(com.example.unisphere.controller.UserController.class)
class UserControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;

    @Test
    void createUserReturnsCreated() throws Exception {
        when(userService.createUser(any())).thenReturn(createUserResponse());

        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "uid": "user123",
                                    "email": "user@example.com",
                                    "displayName": "Alex",
                                    "profileImageUrl": "https://example.com/profile.png"
                                }
                                """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.uid").value("user123"))
                .andExpect(jsonPath("$.email").value("user@example.com"))
                .andExpect(jsonPath("$.displayName").value("Alex"))
                .andExpect(jsonPath("$.profileImageUrl").value("https://example.com/profile.png"));
    }

    @Test
    void createUserReturnsBadRequestWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "uid": "",
                                    "email": "invalid-email",
                                    "displayName": " ",
                                    "profileImageUrl": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void getUserByUidReturnsUser() throws Exception {
        when(userService.getUserByUid("user123")).thenReturn(createUserResponse());

        mockMvc.perform(get("/users/user123"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value("user123"))
                .andExpect(jsonPath("$.displayName").value("Alex"));
    }

    @Test
    void getUserByUidReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        when(userService.getUserByUid("missing"))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(get("/users/missing"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateUserReturnsUpdatedUser() throws Exception {
        UserResponse response = createUserResponse();
        response.setDisplayName("Updated Alex");
        response.setProfileImageUrl("https://example.com/updated.png");

        when(userService.updateUser(eq("user123"), any())).thenReturn(response);

        mockMvc.perform(put("/users/user123")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "displayName": "Updated Alex",
                                    "profileImageUrl": "https://example.com/updated.png"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("Updated Alex"))
                .andExpect(jsonPath("$.profileImageUrl").value("https://example.com/updated.png"));
    }

    @Test
    void updateUserReturnsBadRequestWhenRequestIsInvalid() throws Exception {
        mockMvc.perform(put("/users/user123")
                        .contentType(APPLICATION_JSON)
                        .content("""
                                {
                                    "displayName": " ",
                                    "profileImageUrl": null
                                }
                                """))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userService);
    }

    @Test
    void deleteUserReturnsNoContent() throws Exception {
        mockMvc.perform(delete("/users/user123"))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(userService).deleteUser("user123");
    }

    private UserResponse createUserResponse() {
        UserResponse response = new UserResponse();
        response.setUid("user123");
        response.setEmail("user@example.com");
        response.setDisplayName("Alex");
        response.setProfileImageUrl("https://example.com/profile.png");
        return response;
    }
}
