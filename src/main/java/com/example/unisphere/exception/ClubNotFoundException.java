package com.example.unisphere.exception;

public class ClubNotFoundException extends RuntimeException{
    public ClubNotFoundException(){
        super("Club not found");
    }
}
