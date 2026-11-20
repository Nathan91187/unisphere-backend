package com.example.unisphere.cloudinary;

public enum ImageFolder {

    PROFILE("unisphere/profile-images"), //basically invoking the constructor ImageFolder("some-url");
    CLUB("unisphere/club-images"),
    EVENT("unisphere/event-images");

    private final String path;

    ImageFolder(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}