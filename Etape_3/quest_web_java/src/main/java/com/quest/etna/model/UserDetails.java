package com.quest.etna.model;

import com.quest.etna.enums.UserRole;

import java.io.Serializable;

public class UserDetails implements Serializable {


    private String username;

    private UserRole role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public UserDetails(String username) {
        this.username = username;
        this.role = UserRole.ROLE_USER;
    }

    public UserDetails() {
        this.username = "test";
    }
}
