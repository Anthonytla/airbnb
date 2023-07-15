package com.quest.etna.model;

import java.io.Serializable;

public class AuthResponse implements Serializable {
    private String token;

    public AuthResponse() {}

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
