package com.quest.etna.model;

import com.quest.etna.enums.UserRole;

import java.io.Serializable;

public class RegisterUserDto implements Serializable {
    private String username;

    private String password;

    private UserRole role;

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RegisterUserDto() {
    }

    public RegisterUserDto(String username, String password, UserRole role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
