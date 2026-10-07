package com.example.unisphere.dto.club;

import com.example.unisphere.model.ClubCategory;

public class ClubResponse {
    private Long id;

    private String name;
    private String description;
    private String imageUrl;
    private int memberCount;
    private ClubCategory category;
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
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
    public int getMemberCount() {
        return memberCount;
    }
    public void setMemberCount(int memberCount) {
        this.memberCount = memberCount;
    }
    public ClubCategory getCategory() {
        return category;
    }
    public void setCategory(ClubCategory category) {
        this.category = category;
    }
}
