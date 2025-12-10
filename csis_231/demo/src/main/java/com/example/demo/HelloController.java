package com.example.demo;

import com.example.demo.api.BackendClient;
import com.example.demo.model.AuthResponse;
import com.example.demo.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;

public class HelloController {

    @FXML
    private Label welcomeText;

    @FXML
    private TableView<User> userTable;

    @FXML
    private TableColumn<User, String> usernameColumn;

    @FXML
    private TableColumn<User, String> emailColumn;

    @FXML
    private Button addButton;

    @FXML
    private Button editButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button statsButton;

    private BackendClient api;
    public void setBackendClient(BackendClient backendClient) {
        this.api = backendClient;
    }
    private AuthResponse authResponse;

    private final ObservableList<User> users = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        usernameColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getUsername()));
        emailColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleStringProperty(cellData.getValue().getEmail()));
        userTable.setItems(users);
        usernameColumn.setSortable(true);
        emailColumn.setSortable(true);
    }

    public void initData(BackendClient backendClient, AuthResponse authResponse) {
        this.api = backendClient;
        this.authResponse = authResponse;
        welcomeText.setText("Welcome, " + authResponse.getUsername() + "!");
        reload();
    }

    @FXML
    protected void onAddUser() {
        showUserForm(null);
    }

    @FXML
    protected void onEditUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to edit").showAndWait();
            return;
        }
        showUserForm(selected);
    }

    @FXML
    protected void onDeleteUser() {
        User selected = userTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Please select a user to delete").showAndWait();
            return;
        }
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                "Are you sure you want to delete: " + selected.getUsername() + "?");
        confirmAlert.setTitle("Confirm Delete");
        confirmAlert.setHeaderText("Delete User");
        if (confirmAlert.showAndWait().orElse(null) == ButtonType.OK) {
            try {
                api.deleteUser(selected.getId());
                reload();
            } catch (Exception e) {
                new Alert(Alert.AlertType.ERROR,
                        "Failed to delete user: " + e.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    protected void onRefresh() {
        reload();
    }

    private void showUserForm(User user) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/user-form.fxml"));
            Scene scene = new Scene(loader.load(), 400, 320);
            URL css = getClass().getResource("/com/example/demo/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());
            UserFormController controller = loader.getController();
            controller.setUser(user);
            controller.setOnUserSaved(this::reload);
            Stage stage = new Stage();
            stage.setTitle(user == null ? "Add User" : "Edit User");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open user form: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    public void reload() {
        if (api == null) {
            new Alert(Alert.AlertType.ERROR, "BackendClient is not set! Cannot reload users.").showAndWait();
            return;
        }
        users.clear();
        try {
            List<User> fetched = api.fetchUsers();
            users.addAll(fetched);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to load users: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    protected void onStats() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/animation3d-users-view.fxml"));
            Scene scene = new Scene(loader.load(), 900, 600);
            URL css = getClass().getResource("/com/example/demo/styles.css");
            if (css != null) scene.getStylesheets().add(css.toExternalForm());

            Animation3DUsersController controller = loader.getController();
            controller.setBackendClient(api); // pass the same BackendClient

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setTitle("3D User Statistics");
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to open stats: " + e.getMessage()).showAndWait();
            e.printStackTrace();
        }
    }

    @FXML
    protected void onLogout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/example/demo/fxml/fxml/login.fxml"));
            Scene scene = new Scene(loader.load(), 800, 600);

            Stage stage = (Stage) userTable.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Login");
            stage.show();
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to logout: " + e.getMessage()).showAndWait();
        }
    }

}
