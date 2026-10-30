package com.example.unisphere.controller;

import com.example.unisphere.dto.user.CreateUserRequest;
import com.example.unisphere.dto.user.UpdateUserRequest;
import com.example.unisphere.dto.user.UserResponse;
import com.example.unisphere.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    public UserController(UserService userService){
        this.userService = userService;
    }
    @GetMapping("/{userUid}")
    public UserResponse getUserByUid(@PathVariable String userUid){
        return userService.getUserByUid(userUid);
    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse createUser(@RequestBody @Valid CreateUserRequest request){
        return userService.createUser(request);
    }
    @PutMapping("/me")
    public UserResponse updateUser(
            @Valid @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(request);
    }
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(){
        userService.deleteUser();
    }

}
