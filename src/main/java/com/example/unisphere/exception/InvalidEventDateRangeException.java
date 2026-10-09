package com.example.unisphere.exception;

public class InvalidEventDateRangeException extends RuntimeException {

    public InvalidEventDateRangeException() {
        super("Start date must not be after end date");
    }
}