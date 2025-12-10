package com.example.demo;

import com.example.demo.api.BackendClient;
import com.example.demo.model.AuthResponse;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 900, 600);

        HelloController controller = fxmlLoader.getController();

        BackendClient backendClient = new BackendClient();

        // Create dummy AuthResponse for testing
        AuthResponse dummyAuth = new AuthResponse(
                "dummy-token",
                "Admin",
                1L,
                "admin@gmail.com",
                "ADMIN",
                ""
        );

        backendClient.setAuthToken(dummyAuth.getToken());

        controller.initData(backendClient, dummyAuth);

        scene.getStylesheets().add(getClass().getResource("/com/example/demo/fxml/styles.css").toExternalForm());

        stage.setTitle("User Management System");
        stage.setMinWidth(800);
        stage.setMinHeight(500);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
