package com.example.unisphere.exception;

public class UsernameAlreadyExistsException extends RuntimeException{
    public UsernameAlreadyExistsException() {
        super("Username already exists");
    }
}
