package com.example.unisphere.exception;

public class ClubMembershipAlreadyExistsException extends RuntimeException{
    public ClubMembershipAlreadyExistsException(){
        super("User is already a member of this club");
    }
}
