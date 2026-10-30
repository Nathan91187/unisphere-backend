
package com.example.unisphere.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CreateUserRequest {
        @NotBlank(message = "Display name is required")
        @Size(max = 100, message = "Display name must not exceed 100 characters")
        private String displayName;

        @Size(max = 2048, message = "Profile image URL must not exceed 2048 characters")
        private String profileImageUrl;


    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }
}
