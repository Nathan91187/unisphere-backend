package com.example.unisphere.dto.club;

import com.example.unisphere.model.ClubCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateClubRequest {
    @NotBlank(message = "Club name is required")
    @Size(max = 100, message = "Club name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Club description is required")
    @Size(max = 2000, message = "Club description must not exceed 2000 characters")
    private String description;

    @Size(max = 2048, message = "Image URL must not exceed 2048 characters")
    private String imageUrl;

    @NotNull(message = "Club category is required")
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
