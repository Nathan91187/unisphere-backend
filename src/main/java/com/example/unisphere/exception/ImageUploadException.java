package com.example.unisphere.exception;

public class ImageUploadException extends RuntimeException{
    public ImageUploadException(){
        super("Failed to upload image. Please try again.");
    }
}
