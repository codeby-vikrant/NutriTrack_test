package com.example.nutritrack_test.models;

public class LoginResponse {
    private String access_token;
    private int user_id;

    public LoginResponse(String access_token, int user_id) {
        this.access_token = access_token;
        this.user_id = user_id;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
