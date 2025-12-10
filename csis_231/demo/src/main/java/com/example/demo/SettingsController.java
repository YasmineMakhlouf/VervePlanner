package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SettingsController {

    @FXML
    private ComboBox<String> themeComboBox;

    @FXML
    private CheckBox taskReminderCheckBox;

    @FXML
    private CheckBox dailySummaryCheckBox;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField emailField;

    @FXML
    private Button saveSettingsButton;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {

        backButton.setOnAction(e -> goBack());

        themeComboBox.getItems().addAll("Light", "Dark");
        themeComboBox.getSelectionModel().selectFirst();

        themeComboBox.setOnAction(e -> applyTheme());

        saveSettingsButton.setOnAction(e -> saveSettings());
    }

    private void saveSettings() {
        String theme = themeComboBox.getValue();
        boolean taskReminders = taskReminderCheckBox.isSelected();
        boolean dailySummary = dailySummaryCheckBox.isSelected();
        String username = usernameField.getText();
        String email = emailField.getText();

        System.out.println("Settings saved:");
        System.out.println("Theme: " + theme);
        System.out.println("Task Reminders: " + taskReminders);
        System.out.println("Daily Summary: " + dailySummary);
        System.out.println("Username: " + username);
        System.out.println("Email: " + email);
    }

    private void applyTheme() {
        String selectedTheme = themeComboBox.getValue();

        Scene scene = saveSettingsButton.getScene();
        if (scene == null) return;

        scene.getStylesheets().clear();

        if ("Dark".equals(selectedTheme)) {
            scene.getStylesheets().add(
                    SettingsController.class.getResource("/com/example/demo/dark-theme.css").toExternalForm()
            );

        } else {
            scene.getStylesheets().add(
                    SettingsController.class.getResource("/com/example/demo/styles.css").toExternalForm()
            );

        }
    }

    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/TrackPlannerView.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Track Planner");
            stage.show();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
