package com.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.IOException;

public class AboutUsController {

    @FXML
    private ImageView logoImage;

    @FXML
    private Button backButton;

    @FXML
    public void initialize() {
        backButton.setOnAction(e -> goBack());
        logoImage.setImage(new Image(getClass().getResourceAsStream("/images/verve.png")));
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
