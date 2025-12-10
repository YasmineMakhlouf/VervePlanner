package com.example.demo;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class TrackPlannerController {

    @FXML
    private VBox taskListVBox;

    @FXML
    private Label dateLabel, tasksSummaryLabel, streakLabel;

    @FXML
    private ProgressBar completionProgressBar;

    @FXML
    private Button addTaskButton;

    @FXML
    private Button settingsButton;

    @FXML
    private MenuButton optionsMenu;

    @FXML
    private MenuItem calendarMenuItem;

    @FXML
    private HBox bottlesBox;

    @FXML
    private Label progressLabel;

    @FXML
    private Button someButton;

    @FXML
    private MenuItem privacyPolicyMenuItem;

    @FXML
    private MenuItem termsMenuItem;

    @FXML
    private MenuItem aboutusMenuItem;

    private int completedTasks = 0;
    private int totalTasks = 0;
    private int streak = 3;

    private final Random random = new Random();

    private final int totalBottles = 5;
    private int filledBottles = 0;
    private Rectangle[] bottles = new Rectangle[totalBottles];

    @FXML
    public void initialize() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMM yyyy")));

        addTaskButton.setOnAction(e -> addTask("New Task"));
        settingsButton.setOnAction(e -> openSettings());
        calendarMenuItem.setOnAction(e -> openCalendar());
        privacyPolicyMenuItem.setOnAction(e -> openPrivacyPolicy());
        termsMenuItem.setOnAction(e -> openTerms());
        aboutusMenuItem.setOnAction(e -> openAboutUS());

        addTask("Finish JavaFX project");
        addTask("Workout for 30 mins");
        addTask("Read a chapter of a book");

        initializeWaterTracker();
    }

    private void addTask(String title) {
        HBox taskCard = createTaskCard(title);
        taskListVBox.getChildren().add(taskCard);
        totalTasks++;
        updateProgress();
    }

    private HBox createTaskCard(String title) {
        HBox card = new HBox();
        card.setSpacing(10);
        card.setPadding(new Insets(10));
        card.setStyle("-fx-background-color: " + getRandomColor() + "; -fx-background-radius: 10; -fx-border-radius: 10; -fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 4, 0, 2, 2);");

        CheckBox checkBox = new CheckBox();
        Label taskLabel = new Label(title);
        taskLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: black;");

        taskLabel.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                TextInputDialog dialog = new TextInputDialog(taskLabel.getText());
                dialog.setTitle("Edit Task");
                dialog.setHeaderText("✏️ Edit your task name:");
                dialog.setContentText("Task:");
                dialog.getDialogPane().getStylesheets().add(
                        getClass().getResource("/com/example/demo/fxml/dialog.css").toExternalForm()
                );
                dialog.showAndWait().ifPresent(taskLabel::setText);
            }
        });

        Button deleteButton = new Button("🗑");
        deleteButton.setStyle("-fx-background-color: transparent; -fx-font-size: 16px;");
        deleteButton.setOnAction(e -> {
            taskListVBox.getChildren().remove(card);
            totalTasks--;
            if (checkBox.isSelected()) completedTasks--;
            updateProgress();
        });

        checkBox.setOnAction(e -> {
            if (checkBox.isSelected()) completedTasks++;
            else completedTasks--;
            playFadeAnimation(taskLabel);
            updateProgress();
        });

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        card.getChildren().addAll(checkBox, taskLabel, spacer, deleteButton);
        enableDrag(card);
        return card;
    }

    private void updateProgress() {
        completionProgressBar.setProgress(totalTasks == 0 ? 0 : (double) completedTasks / totalTasks);
        tasksSummaryLabel.setText(completedTasks + " / " + totalTasks + " tasks completed");
        streakLabel.setText("🔥 Streak: " + streak + " days");
    }

    private void playFadeAnimation(Label label) {
        FadeTransition ft = new FadeTransition(Duration.millis(300), label);
        ft.setFromValue(0.3);
        ft.setToValue(1.0);
        ft.play();
    }

    private String getRandomColor() {
        String[] colors = {"#ffd6e0", "#ffe6a7", "#caffbf", "#9bf6ff", "#bdb2ff"};
        return colors[random.nextInt(colors.length)];
    }

    private void enableDrag(HBox card) {
        final double[] dragDelta = new double[1];
        card.setOnMousePressed(event -> dragDelta[0] = event.getSceneY() - card.getLayoutY());
        card.setOnMouseDragged(event -> card.setLayoutY(event.getSceneY() - dragDelta[0]));
    }

    private void openSettings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/Settings.fxml"));
            Stage stage = (Stage) settingsButton.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Settings");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openCalendar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/CalendarView.fxml"));
            Stage stage = (Stage) optionsMenu.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Calendar");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initializeWaterTracker() {
        for (int i = 0; i < totalBottles; i++) {
            StackPane bottlePane = new StackPane();
            Rectangle bottle = new Rectangle(40, 80, Color.LIGHTGRAY);
            bottle.setArcWidth(12);
            bottle.setArcHeight(12);

            Rectangle water = new Rectangle(40, 0, Color.DODGERBLUE);
            water.setArcWidth(12);
            water.setArcHeight(12);
            water.setTranslateY(40);
            bottles[i] = water;

            bottlePane.getChildren().addAll(bottle, water);
            int index = i;
            bottlePane.setOnMouseClicked(e -> fillBottle(index));

            bottlesBox.getChildren().add(bottlePane);
        }
        updateWaterProgress();
    }

    private void fillBottle(int index) {
        if (bottles[index].getHeight() == 0) {
            bottles[index].setHeight(80);
            bottles[index].setTranslateY(0);
            filledBottles++;
        } else {
            bottles[index].setHeight(0);
            bottles[index].setTranslateY(40);
            filledBottles--;
        }
        updateWaterProgress();
    }

    private void updateWaterProgress() {
        progressLabel.setText(filledBottles + " / " + totalBottles + " glasses drank");
    }

    @FXML
    void openStats() throws IOException {
        Stage stage = (Stage) someButton.getScene().getWindow();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/CalendarStatsView.fxml"));
        Parent root = loader.load();

        stage.setScene(new Scene(root));
        stage.show();
    }

    private void openPrivacyPolicy() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/PrivacyPolicy.fxml"));
            Stage stage = (Stage) optionsMenu.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Privacy Policy");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openTerms() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/TermsConditions.fxml"));
            Stage stage = (Stage) optionsMenu.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("Terms and Conditions");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void openAboutUS() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/AboutUS.fxml"));
            Stage stage = (Stage) optionsMenu.getScene().getWindow();
            stage.setScene(new Scene(loader.load()));
            stage.setTitle("AboutUS");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            Stage stage = (Stage) taskListVBox.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Logout failed: " + e.getMessage()).showAndWait();
        }
    }


}
