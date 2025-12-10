package com.example.demo.api;

import com.example.demo.model.AuthResponse;
import com.example.demo.model.LoginRequest;
import com.example.demo.model.RegisterRequest;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Properties;

public class AuthClient {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;

    public AuthClient() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/client.properties")) {
            if (is == null) throw new RuntimeException("client.properties not found in resources");
            props.load(is);
            this.baseUrl = props.getProperty("backend.baseUrl", "http://localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load client.properties", e);
        }
    }

    public AuthResponse login(LoginRequest request) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), AuthResponse.class);
        } else {
            throw new RuntimeException("Login failed: " + response.body());
        }
    }

    public AuthResponse register(RegisterRequest request) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(request);
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/register"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            return mapper.readValue(response.body(), AuthResponse.class);
        } else {
            throw new RuntimeException("Registration failed: " + response.body());
        }
    }

    public boolean validateToken(String token) throws IOException, InterruptedException {
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/auth/validate"))
                .header("Authorization", "Bearer " + token)
                .GET()
                .build();

        HttpResponse<String> response = http.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        return response.statusCode() == 200;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
