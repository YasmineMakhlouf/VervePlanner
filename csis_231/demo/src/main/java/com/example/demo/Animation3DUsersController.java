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
    private double mousePosX, mousePosY;
    private double mouseOldX, mouseOldY;
    private final Rotate rotateX = new Rotate(0, Rotate.X_AXIS);
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
        camera = new PerspectiveCamera(true);
        camera.setNearClip(0.1);
        camera.setFarClip(10000.0);
        camera.setFieldOfView(45);
        rotateX.setAngle(-20);
        camera.getTransforms().addAll(new Translate(0, -200, -800), rotateX, rotateY);

        AmbientLight ambientLight = new AmbientLight(Color.WHITE);
        root3D.getChildren().add(ambientLight);

        PointLight pointLight1 = new PointLight(Color.WHITE);
        pointLight1.setTranslateX(-500); pointLight1.setTranslateY(-500); pointLight1.setTranslateZ(-500);
        PointLight pointLight2 = new PointLight(Color.LIGHTBLUE);
        pointLight2.setTranslateX(500); pointLight2.setTranslateY(500); pointLight2.setTranslateZ(500);

        root3D.getChildren().addAll(pointLight1, pointLight2);

        SubScene subScene = new SubScene(root3D, 800, 600, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setFill(Color.rgb(20, 20, 30));

        subScene.setOnMousePressed(me -> {
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            mouseOldX = me.getSceneX();
            mouseOldY = me.getSceneY();
        });

        subScene.setOnMouseDragged(me -> {
            mouseOldX = mousePosX;
            mouseOldY = mousePosY;
            mousePosX = me.getSceneX();
            mousePosY = me.getSceneY();
            double deltaX = (mousePosX - mouseOldX);
            double deltaY = (mousePosY - mouseOldY);
            rotateY.setAngle(rotateY.getAngle() + deltaX * 0.5);
            rotateX.setAngle(rotateX.getAngle() - deltaY * 0.5);
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
            if (statusLabel != null) statusLabel.setText("Loading user data...");
            List<User> users = api.fetchUsers();
            if (statusLabel != null) statusLabel.setText("Loaded " + users.size() + " users. Visualizing...");

            // Clear previous visualization
            for (Group g : userGroups) root3D.getChildren().remove(g);
            userGroups.clear();

            if (users.isEmpty()) {
                if (statusLabel != null) statusLabel.setText("No users found!");
                return;
            }

            visualizeUsers(users);
            if (statusLabel != null) statusLabel.setText("Displaying " + users.size() + " users in 3D");
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
        double spacing = Math.max(120, Math.min(200, 1500 / Math.max(cols, rows)));
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
        double barHeight = 30 + nameLength * 8;
        double barWidth = 40;
        double barDepth = 40;

        Box bar = new Box(barWidth, barHeight, barDepth);
        int colorSeed = user.getId() != null ? user.getId().intValue() : user.getUsername().hashCode();
        random.setSeed(colorSeed);
        Color baseColor = Color.hsb((colorSeed % 360), 0.8 + random.nextDouble() * 0.2, 0.7 + random.nextDouble() * 0.3);

        PhongMaterial material = new PhongMaterial();
        material.setDiffuseColor(baseColor);
        material.setSpecularColor(Color.WHITE);
        material.setSpecularPower(32.0);
        bar.setMaterial(material);

        bar.setTranslateX(x);
        bar.setTranslateY(-barHeight / 2);
        bar.setTranslateZ(z);

        RotateTransition rt = new RotateTransition(Duration.seconds(4 + index * 0.2), bar);
        rt.setAxis(Rotate.Y_AXIS);
        rt.setFromAngle(-5);
        rt.setToAngle(5);
        rt.setAutoReverse(true);
        rt.setCycleCount(javafx.animation.Animation.INDEFINITE);
        rt.play();

        group.getChildren().add(bar);
        createUserTextLabel(user, x, barHeight, z, group);

        Box base = new Box(barWidth + 10, 5, barDepth + 10);
        PhongMaterial baseMaterial = new PhongMaterial(Color.GRAY);
        base.setMaterial(baseMaterial);
        base.setTranslateX(x);
        base.setTranslateY(2.5);
        base.setTranslateZ(z);
        group.getChildren().add(base);

        return group;
    }

    private void createUserTextLabel(User user, double x, double barHeight, double z, Group parent) {
        String displayName = user.getUsername() != null ? user.getUsername() : "Unknown";
        if (displayName.length() > 15) displayName = displayName.substring(0, 12) + "...";

        Text text = new Text(displayName);
        text.setFill(Color.WHITE);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 12));

        double estimatedWidth = displayName.length() * 7;
        double estimatedHeight = 18;

        Rectangle bg = new Rectangle();
        bg.setFill(Color.rgb(0, 0, 0, 0.7));
        bg.setStroke(Color.WHITE);
        bg.setStrokeWidth(1);
        bg.setWidth(Math.max(estimatedWidth + 10, 80));
        bg.setHeight(estimatedHeight);
        bg.setX(-bg.getWidth() / 2);
        bg.setY(-bg.getHeight() / 2);

        text.setX(-estimatedWidth / 2);
        text.setY(estimatedHeight / 4);

        Group labelGroup = new Group();
        labelGroup.getChildren().addAll(bg, text);
        labelGroup.setTranslateX(x);
        labelGroup.setTranslateY(-barHeight - 40);
        labelGroup.setTranslateZ(z);

        parent.getChildren().add(labelGroup);
    }

    private void createBasePlatform(int cols, int rows, double spacing, double startX, double startZ) {
        if (platform != null) root3D.getChildren().remove(platform);
        double platformWidth = cols * spacing + 100;
        double platformDepth = rows * spacing + 100;

        platform = new Box(platformWidth, 5, platformDepth);
        PhongMaterial platformMaterial = new PhongMaterial(Color.rgb(50, 50, 60));
        platform.setMaterial(platformMaterial);
        platform.setTranslateX(startX + (cols - 1) * spacing / 2);
        platform.setTranslateY(2.5);
        platform.setTranslateZ(startZ + (rows - 1) * spacing / 2);

        root3D.getChildren().add(platform);
    }
}
