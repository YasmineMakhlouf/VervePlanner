package com.example.demo.api;

import com.example.demo.model.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Properties;

public class BackendClient {

    private final HttpClient http = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String baseUrl;
    private String authToken; // JWT token

    public BackendClient() {
        Properties props = new Properties();
        try (InputStream is = getClass().getResourceAsStream("/client.properties")) {
            if (is == null) throw new RuntimeException("client.properties not found in resources");
            props.load(is);
            this.baseUrl = props.getProperty("backend.baseUrl", "http://localhost:8080");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load client.properties", e);
        }
        System.out.println("Backend URL: " + this.baseUrl); // Debugging
    }

    public void setAuthToken(String token) {
        this.authToken = token;
    }

    public void clearAuthToken() {
        this.authToken = null;
    }

    private HttpRequest.Builder withAuth(HttpRequest.Builder builder) {
        if (authToken != null) {
            builder.header("Authorization", "Bearer " + authToken);
        }
        return builder;
    }

    // ======================
    // USER API METHODS
    // ======================

    public List<User> fetchUsers() throws IOException, InterruptedException {
        HttpRequest req = withAuth(HttpRequest.newBuilder())
                .uri(URI.create(baseUrl + "/api/users"))
                .GET()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), new TypeReference<>() {});
    }

    public User getUser(Long id) throws IOException, InterruptedException {
        HttpRequest req = withAuth(HttpRequest.newBuilder())
                .uri(URI.create(baseUrl + "/api/users/" + id))
                .GET()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), User.class);
    }

    public User createUser(User user) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(user);
        HttpRequest req = withAuth(HttpRequest.newBuilder())
                .uri(URI.create(baseUrl + "/api/users"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), User.class);
    }

    public User updateUser(Long id, User user) throws IOException, InterruptedException {
        String json = mapper.writeValueAsString(user);
        HttpRequest req = withAuth(HttpRequest.newBuilder())
                .uri(URI.create(baseUrl + "/api/users/" + id))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(json))
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        return mapper.readValue(resp.body(), User.class);
    }

    public void deleteUser(Long id) throws IOException, InterruptedException {
        HttpRequest req = withAuth(HttpRequest.newBuilder())
                .uri(URI.create(baseUrl + "/api/users/" + id))
                .DELETE()
                .build();

        HttpResponse<String> resp = http.send(req, HttpResponse.BodyHandlers.ofString());
        if (resp.statusCode() != 204) {
            throw new RuntimeException("Delete failed with status: " + resp.statusCode());
        }
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getBaseUrl() {
        return baseUrl;
    }
}
