package com.example.unisphere.model;
import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {
    @Id
    private String uid;
    @Column(nullable = false,unique = true)
    private String email;
    @Column(nullable = false)
    private String displayName;
    private String profileImageUrl;
    @Column(nullable = false,unique = true)
    private String username;
    public String getUid() {
        return uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
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
