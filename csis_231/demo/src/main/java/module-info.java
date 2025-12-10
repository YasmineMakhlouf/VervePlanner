module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.net.http;
    requires com.fasterxml.jackson.databind;
    requires java.desktop;
    requires javafx.graphics;


    opens com.example.demo.model to com.fasterxml.jackson.databind, javafx.base;
    exports com.example.demo;
    opens com.example.demo to com.fasterxml.jackson.databind, javafx.base, javafx.fxml;
}