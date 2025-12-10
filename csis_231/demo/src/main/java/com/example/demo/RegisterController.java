package com.example.demo;

import com.example.demo.model.RegisterRequest;
import com.example.demo.model.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RegisterController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label errorLabel;

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @FXML
    private void handleRegister() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        errorLabel.setText("");

        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            errorLabel.setText("Please fill all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            errorLabel.setText("Passwords do not match");
            return;
        }

        sendRegisterRequest(new RegisterRequest(username, email, password));
    }

    private void sendRegisterRequest(RegisterRequest requestBody) {
        try {
            String json = objectMapper.writeValueAsString(requestBody);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("http://localhost:8080/api/auth/register"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();

            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenAccept(response -> Platform.runLater(() -> {
                        if (response.statusCode() == 200) {
                            try {
                                AuthResponse resp = objectMapper.readValue(response.body(), AuthResponse.class);
                                openTrackPlanner();
                            } catch (IOException e) {
                                errorLabel.setText("Failed to parse server response");
                            }
                        } else {
                            try {
                                SpringError springError = objectMapper.readValue(response.body(), SpringError.class);
                                errorLabel.setText(springError.message != null ? springError.message : "Registration failed");
                            } catch (Exception ex) {
                                errorLabel.setText("Registration failed: " + response.body());
                            }
                        }
                    }))
                    .exceptionally(e -> {
                        Platform.runLater(() -> errorLabel.setText("Connection failed: " + e.getMessage()));
                        return null;
                    });

        } catch (Exception e) {
            errorLabel.setText("Error: " + e.getMessage());
        }
    }

    private void openTrackPlanner() {
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
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class SpringError {
        public String timestamp;
        public int status;
        public String error;
        public String message;
        public java.util.List<String> errors;
    }
}
