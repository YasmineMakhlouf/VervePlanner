package com.example.demo;

import com.example.demo.api.BackendClient;
import com.example.demo.model.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class LoginController {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();
        errorLabel.setText("");

        if ("admin@gmail.com".equalsIgnoreCase(username)) {
            openHelloController();
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please fill all fields");
            return;
        }

        try {
            sendLoginRequest(username, password);
        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    private void sendLoginRequest(String username, String password) {
        String json = """
                {
                    "username": "%s",
                    "password": "%s"
                }
                """.formatted(username, password);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/api/auth/login"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenAccept(response -> Platform.runLater(() -> {
                    if (response.statusCode() == 200) {
                        try {
                            AuthResponse authResponse = objectMapper.readValue(response.body(), AuthResponse.class);
                            openTrackPlanner(authResponse);
                        } catch (IOException e) {
                            errorLabel.setText("Failed to parse response");
                        }
                    } else {
                        errorLabel.setText("Invalid username or password!");
                    }
                }))
                .exceptionally(e -> {
                    Platform.runLater(() -> errorLabel.setText("Connection failed: " + e.getMessage()));
                    return null;
                });
    }

    private void openTrackPlanner(AuthResponse authResponse) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/TrackPlannerView.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Track Planner");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/register.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openHelloController() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/hello-view.fxml"));
            BorderPane root = loader.load();

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root, 900, 600));

            HelloController controller = loader.getController();
            BackendClient backendClient = new BackendClient();

            AuthResponse adminAuth = new AuthResponse(
                    "dummy-token",
                    "Admin",
                    1L,
                    "Admin",
                    "admin@gmail.com",
                    "ADMIN"
            );

            backendClient.setAuthToken(adminAuth.getToken());
            controller.initData(backendClient, adminAuth);

            stage.setTitle("User Management System");
            stage.setMinWidth(800);
            stage.setMinHeight(500);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
