package com.example.goaltrackerapplication;

public class UserModel {
    private String id;
    private String name;
    private String email;
    private String profileurl;

    public UserModel(String id, String name, String email, String profileurl) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.profileurl = profileurl;
    }

    @Override
    public String toString() {
        return "UserModel{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", profileurl='" + profileurl + '\'' +
                '}';
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getProfileurl() {
        return profileurl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setProfileurl(String profileurl) {
        this.profileurl = profileurl;
    }
}

