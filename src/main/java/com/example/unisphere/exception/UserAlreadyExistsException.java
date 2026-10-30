package com.example.unisphere.exception;

public class UserAlreadyExistsException extends RuntimeException {

    public UserAlreadyExistsException() {
        super("User profile already exists");
    }
}
