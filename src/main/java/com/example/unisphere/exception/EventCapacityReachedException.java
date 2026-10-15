package com.example.unisphere.exception;

public class EventCapacityReachedException extends RuntimeException{
    public EventCapacityReachedException(){
        super("This event has reached its maximum capacity");
    }
}
