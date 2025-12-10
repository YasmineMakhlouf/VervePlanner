package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PersonalHubController {

    @FXML
    private Button backButton;

    @FXML
    private ComboBox<String> moodComboBox;

    @FXML
    private Button saveMoodButton;

    @FXML
    private Label moodStatusLabel;

    @FXML
    private TextArea noteTextArea;

    @FXML
    private VBox notesBox;

    @FXML
    private Button addNoteButton;

    @FXML
    private TextField habitTextField;

    @FXML
    private VBox habitsBox;

    @FXML
    private Button addHabitButton;

    @FXML
    private TextField promptTextField;

    @FXML
    private Button savePromptButton;

    @FXML
    private Label promptStatusLabel;

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> backToPlanner());

        moodComboBox.getItems().addAll("😀 Happy", "😐 Neutral", "😞 Sad", "😡 Angry");
        saveMoodButton.setOnAction(e -> {
            String mood = moodComboBox.getValue();
            if (mood != null) moodStatusLabel.setText("Current mood: " + mood);
        });

        addNoteButton.setOnAction(e -> addNote());
        addHabitButton.setOnAction(e -> addHabit());
        savePromptButton.setOnAction(e -> savePrompt());
    }

    private void backToPlanner() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/TrackPlannerView.fxml"));
            Scene scene = new Scene(loader.load(), 900, 700); // Match your TrackPlannerView size
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Track Planner");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Failed to go back to Planner: " + e.getMessage()).showAndWait();
        }
    }

    private void addNote() {
        String note = noteTextArea.getText().trim();
        if (!note.isEmpty()) {
            Label noteLabel = new Label("• " + note);
            notesBox.getChildren().add(noteLabel);
            noteTextArea.clear();
        }
    }

    private void addHabit() {
        String habit = habitTextField.getText().trim();
        if (!habit.isEmpty()) {
            CheckBox habitCheckBox = new CheckBox(habit);
            habitsBox.getChildren().add(habitCheckBox);
            habitTextField.clear();
        }
    }

    private void savePrompt() {
        String prompt = promptTextField.getText().trim();
        if (!prompt.isEmpty()) {
            promptStatusLabel.setText("Prompt: " + prompt);
            promptTextField.clear();
        }
    }
}
