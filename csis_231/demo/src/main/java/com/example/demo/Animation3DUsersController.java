package com.example.demo;

import com.example.demo.api.BackendClient;
import com.example.demo.model.User;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Translate;
import javafx.util.Duration;

import java.util.List;
import java.util.Random;

public class Animation3DUsersController {

    @FXML
    private BorderPane rootPane;
    @FXML
    private Button refreshButton;
    @FXML
    private Button backButton;
    @FXML
    private Label statusLabel;

    private BackendClient api;
    private Group root3D;
    private PerspectiveCamera camera;

    private Box platform;
    private final java.util.List<Group> userGroups = new java.util.ArrayList<>();

    // Mouse rotation
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(-20, Rotate.X_AXIS);
    private final Rotate rotateY = new Rotate(0, Rotate.Y_AXIS);

    public void setBackendClient(BackendClient backendClient) {
        this.api = backendClient;
    }

    @FXML
    public void initialize() {
        if (api == null) api = new BackendClient();

        javafx.application.Platform.runLater(() -> {
            try {
                setup3DScene();
                loadAndVisualizeData();
            } catch (Exception e) {
                if (statusLabel != null) statusLabel.setText("Error: " + e.getMessage());
                new Alert(Alert.AlertType.ERROR, "Failed to initialize 3D view: " + e.getMessage()).showAndWait();
            }
        });
    }

    private void setup3DScene() {
        root3D = new Group();

        // Lighting
        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        PointLight light1 = new PointLight(Color.WHITE);
        light1.setTranslateX(-500);
        light1.setTranslateY(-500);
        light1.setTranslateZ(-500);

        PointLight light2 = new PointLight(Color.LIGHTPINK);
        light2.setTranslateX(500);
        light2.setTranslateY(500);
        light2.setTranslateZ(500);

        root3D.getChildren().addAll(ambientLight, light1, light2);
        root3D.getTransforms().addAll(rotateX, rotateY);

        // Camera
        camera = new PerspectiveCamera(true);
        camera.getTransforms().add(new Translate(0, -200, -900));
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);

        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.rgb(0, 0, 0));

        subScene.setOnMousePressed(event -> {
            mouseOldX = event.getX();
            mouseOldY = event.getY();
        });

        subScene.setOnMouseDragged(event -> {
            double deltaX = event.getX() - mouseOldX;
            double deltaY = event.getY() - mouseOldY;

            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.4);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.4);

            mouseOldX = event.getX();
            mouseOldY = event.getY();
        });

        rootPane.setCenter(subScene);
    }

    @FXML
    private void onRefresh() {
        loadAndVisualizeData();
    }

    @FXML
    private void onBack() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(
                    getClass().getResource("/com/example/demo/fxml/fxml/hello-view.fxml"));
            javafx.scene.Scene scene = new javafx.scene.Scene(loader.load(), 900, 600);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo/fxml/styles.css").toExternalForm());
            HelloController controller = loader.getController();
            controller.setBackendClient(api);
            javafx.stage.Stage stage = (javafx.stage.Stage) backButton.getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("User Management");
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Failed to go back: " + e.getMessage()).showAndWait();
        }
    }

    private void loadAndVisualizeData() {
        try {
            if (statusLabel != null) statusLabel.setText("Loading users...");
            List<User> users = api.fetchUsers();

            if (statusLabel != null)
                statusLabel.setText("Loaded " + users.size() + " users. Visualizing...");

            for (Group g : userGroups) root3D.getChildren().remove(g);
            userGroups.clear();

            if (users.isEmpty()) {
                statusLabel.setText("No users found");
                return;
            }

            visualizeUsers(users);

            if (statusLabel != null)
                statusLabel.setText("3D visualization complete");

        } catch (Exception e) {
            if (statusLabel != null) statusLabel.setText("Error: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Failed to load users: " + e.getMessage()).showAndWait();
        }
    }

    private void visualizeUsers(List<User> users) {
        Random random = new Random();
        int count = users.size();

        int cols = (int) Math.ceil(Math.sqrt(count));
        int rows = (int) Math.ceil((double) count / cols);

        double spacing = 150;
        double startX = -(cols - 1) * spacing / 2;
        double startZ = -(rows - 1) * spacing / 2;

        createBasePlatform(cols, rows, spacing, startX, startZ);

        for (int i = 0; i < count; i++) {
            User user = users.get(i);
            int col = i % cols;
            int row = i / cols;

            double x = startX + col * spacing;
            double z = startZ + row * spacing;

            Group userGroup = createUserBar(user, i, x, z, random);

            root3D.getChildren().add(userGroup);
            userGroups.add(userGroup);
        }
    }

    private Group createUserBar(User user, int index, double x, double z, Random random) {
        Group group = new Group();

        int nameLength = user.getUsername() != null ? user.getUsername().length() : 5;
        double barHeight = 40 + nameLength * 7;

        Box bar = new Box(40, barHeight, 40);

        // Pink color for all cubes
        PhongMaterial pinkMaterial = new PhongMaterial(Color.HOTPINK);
        bar.setMaterial(pinkMaterial);

        bar.setTranslateX(x);
        bar.setTranslateY(-barHeight / 2);
        bar.setTranslateZ(z);

        RotateTransition rt = new RotateTransition(Duration.seconds(4), bar);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(-5);
        rt.setToAngle(5);
        rt.setAutoReverse(true);
        rt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rt.play();

        group.getChildren().add(bar);
        createUserTextLabel(user, x, barHeight, z, group);

        Box base = new Box(50, 5, 50);
        base.setMaterial(new PhongMaterial(Color.BLACK)); // Base black
        base.setTranslateX(x);
        base.setTranslateY(2.5);
        base.setTranslateZ(z);
        group.getChildren().add(base);

        return group;
    }

    private void createUserTextLabel(User user, double x, double barHeight, double z, Group parent) {
        String name = user.getUsername() != null ? user.getUsername() : "Unknown";
        if (name.length() > 15) name = name.substring(0, 12) + "...";

        Text text = new Text(name);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        text.setFill(Color.WHITE);

        double width = text.getLayoutBounds().getWidth();
        double padding = 10;

        Rectangle bg = new Rectangle(width + padding * 2, 22);
        bg.setFill(Color.rgb(0, 0, 0, 0.7));
        bg.setStroke(Color.HOTPINK); // Pink border for text

        text.setTranslateX(-width / 2);
        text.setTranslateY(7);

        bg.setTranslateX(-bg.getWidth() / 2);
        bg.setTranslateY(0);

        Group g = new Group(bg, text);
        g.setTranslateX(x);
        g.setTranslateY(-barHeight - 35);
        g.setTranslateZ(z);

        parent.getChildren().add(g);
    }

    private void createBasePlatform(int cols, int rows, double spacing, double startX, double startZ) {
        if (platform != null) root3D.getChildren().remove(platform);

        double width = cols * spacing + 150;
        double depth = rows * spacing + 150;

        platform = new Box(width, 5, depth);
        platform.setMaterial(new PhongMaterial(Color.BLACK)); // Platform black
        platform.setTranslateX(startX + (cols - 1) * spacing / 2);
        platform.setTranslateY(2.5);
        platform.setTranslateZ(startZ + (rows - 1) * spacing / 2);

        root3D.getChildren().add(platform);
    }
}
