package com.example.unisphere.dto.club_dto;

import com.example.unisphere.model.ClubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class CreateClubRequest {
    @NotBlank
    private String name;
    @NotBlank
    private String description;
    @NotBlank
    private String imageUrl;
    @NotNull
    private ClubCategory category;
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public ClubCategory getCategory() {
        return category;
    }
    public void setCategory(ClubCategory category) {
        this.category = category;
    }
}
