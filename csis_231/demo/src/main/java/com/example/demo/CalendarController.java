package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.scene.Scene;

import java.io.IOException;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;

public class CalendarController {

    @FXML
    private GridPane calendarGrid;

    @FXML
    private Label monthLabel;

    @FXML
    private Button prevMonthButton, nextMonthButton;

    @FXML
    private Slider zoomSlider;

    @FXML
    private Button backButton;

    private YearMonth currentYearMonth;
    private double cellSize = 100;

    private final HashMap<LocalDate, String> notes = new HashMap<>();

    @FXML
    public void initialize() {

        backButton.setOnAction(e -> goBack());

        currentYearMonth = YearMonth.now();
        updateCalendar();

        prevMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.minusMonths(1);
            updateCalendar();
        });

        nextMonthButton.setOnAction(e -> {
            currentYearMonth = currentYearMonth.plusMonths(1);
            updateCalendar();
        });

        zoomSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            cellSize = newVal.doubleValue();
            updateCalendar();
        });
    }

    private void updateCalendar() {
        calendarGrid.getChildren().clear();
        monthLabel.setText(currentYearMonth.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + currentYearMonth.getYear());

        LocalDate firstDayOfMonth = currentYearMonth.atDay(1);
        int dayOfWeek = firstDayOfMonth.getDayOfWeek().getValue();
        int daysInMonth = currentYearMonth.lengthOfMonth();

        int col = dayOfWeek % 7;
        int row = 0;

        for (int day = 1; day <= daysInMonth; day++) {
            LocalDate date = currentYearMonth.atDay(day);
            VBox dayBox = createDayBox(date);

            calendarGrid.add(dayBox, col, row);

            col++;
            if (col > 6) {
                col = 0;
                row++;
            }
        }
    }

    private VBox createDayBox(LocalDate date) {
        VBox box = new VBox();
        box.setPrefSize(cellSize, cellSize);
        box.setStyle("-fx-border-color: #ccc; -fx-background-color: #fff;");
        box.setSpacing(5);
        box.setPadding(new javafx.geometry.Insets(5));

        Label dayLabel = new Label(String.valueOf(date.getDayOfMonth()));
        dayLabel.setStyle("-fx-font-weight: bold;");

        Text noteText = new Text(notes.getOrDefault(date, ""));
        noteText.wrappingWidthProperty().bind(box.widthProperty());

        box.getChildren().addAll(dayLabel, noteText);

        box.setOnMouseClicked(event -> openNoteDialog(date, noteText));

        return box;
    }

    private void openNoteDialog(LocalDate date, Text noteText) {
        TextInputDialog dialog = new TextInputDialog(notes.getOrDefault(date, ""));
        dialog.setTitle("Add Note");
        dialog.setHeaderText("Write your note for " + date.toString());
        dialog.setContentText("Note:");

        dialog.showAndWait().ifPresent(text -> {
            notes.put(date, text);
            noteText.setText(text);
        });
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
