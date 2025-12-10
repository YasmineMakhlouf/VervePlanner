package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
    private Long id;
    private String username;
    private String email;
    private String role;

    public User() {}

    public User(
            @JsonProperty("id") Long id,
            @JsonProperty("username") String username,
            @JsonProperty("email") String email
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
    }

    // getters/setters

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String toString() {
        return username + " (" + email + ")";
    }
}
