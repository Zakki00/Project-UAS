package com.mycompany.Model;

public class GoogleUser {

    private String id;
    private String email;
    private String name;
    private String profilePictureUrl;

    public GoogleUser() {
    }

    public GoogleUser(String id, String email, String name, String profilePictureUrl) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.profilePictureUrl = profilePictureUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }
}