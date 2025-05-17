package com.app.f1xdesktop;

import com.app.f1xdesktop.utils.Constants;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class F1xApplication extends Application {

        public static Scene scene;
        public static VBox root;

        private Stage primaryStage;

        @Override
        public void start(Stage primaryStage) throws Exception {
                this.primaryStage = primaryStage;

                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(Constants.UI_LAYOUT_PATH)));

                Font.loadFont(Objects.requireNonNull(getClass().getResource(Constants.FONT_PATH)).toExternalForm(), 12);

                scene = new Scene(root, 800, 600);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(Constants.CSS_PATH)).toExternalForm());
                scene.setFill(Color.web(Constants.BACKGROUND_COLOR));

                primaryStage.setTitle(Constants.TITLE);
                primaryStage.setMaximized(true);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(scene);

                List<Image> icons = new ArrayList<>();
                icons.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.ICON_16_PATH))));
                icons.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.ICON_32_PATH))));
                icons.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.ICON_48_PATH))));
                icons.add(new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.ICON_64_PATH))));
                primaryStage.getIcons().addAll(icons);

                setInitialScale();
                primaryStage.show();

                restoreStageState();
                addMinimizedListener();
        }

        private void setInitialScale() {
                Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
                root.setScaleX(Constants.WIDTH / bounds.getWidth());
                root.setScaleY(Constants.HEIGHT / bounds.getHeight());
        }

        private void addMinimizedListener() {
                if (primaryStage == null) return;

                primaryStage.iconifiedProperty().addListener((obs, oldVal, newVal) -> {
                        if (!newVal) restoreStageState();
                });
        }

        private void restoreStageState() {
                if (primaryStage == null || root == null) return;

                FadeTransition fade = new FadeTransition(Duration.seconds(0.15), root);
                fade.setToValue(1.0);

                ScaleTransition scale = new ScaleTransition(Duration.seconds(0.15), root);
                scale.setToX(1.0);
                scale.setToY(1.0);

                new ParallelTransition(fade, scale).play();
        }

        public static void main(String[] args) {
                launch(args);
        }
}