package com.example.cb.payload;

import java.util.List;

//output to the auth request
public class LoginResponse {

    private final String jwt;
    private final String type = "Bearer";
    private final Long user_id;
    private final String username;
    private final String email;
    private final List<String> roles;




    public LoginResponse(String jwt, Long id, String username, String email, List<String> roles) {
        this.jwt = jwt;
        this.user_id = id;
        this.username = username;
        this.email = email;
        this.roles = roles;
    }

    public String getJwt() {
        return jwt;
    }

    public String getType() {
        return type;
    }

    public Long getUser_id() {
        return user_id;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }


}
