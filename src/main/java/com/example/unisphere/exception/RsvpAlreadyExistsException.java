package com.example.unisphere.exception;

public class RsvpAlreadyExistsException extends RuntimeException{
    public RsvpAlreadyExistsException(){
        super("User has already rsvp'd for this event");
    }
}
