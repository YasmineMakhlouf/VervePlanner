package com.example.demo;

import com.example.demo.api.BackendClient;
import com.example.demo.model.User;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class UserFormController {

    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private Button saveButton;
    @FXML private Button cancelButton;

    private final BackendClient api = new BackendClient();
    private User user;
    private boolean isEditMode = false;
    private Runnable onUserSaved;

    public void setUser(User user) {
        this.user = user;
        isEditMode = user != null;

        if (isEditMode) {
            usernameField.setText(user.getUsername());
            emailField.setText(user.getEmail());
            saveButton.setText("Update");
        } else {
            usernameField.clear();
            emailField.clear();
            saveButton.setText("Create");
        }
    }

    public void setOnUserSaved(Runnable cb) {
        this.onUserSaved = cb;
    }

    @FXML
    private void onSave() {
        String username = usernameField.getText().trim();
        String email = emailField.getText().trim();

        if (username.isEmpty() || email.isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "All fields required").showAndWait();
            return;
        }

        try {
            if (isEditMode) {
                user.setUsername(username);
                user.setEmail(email);
                api.updateUser(user.getId(), user);
            } else {
                User newUser = new User();
                newUser.setUsername(username);
                newUser.setEmail(email);
                api.createUser(newUser);
            }

            if (onUserSaved != null) onUserSaved.run();
            closeForm();

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Save failed: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    private void onCancel() {
        closeForm();
    }

    private void closeForm() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }
}
