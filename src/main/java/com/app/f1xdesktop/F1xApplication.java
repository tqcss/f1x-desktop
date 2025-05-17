package com.app.f1xdesktop;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.Objects;

public class F1xApplication extends Application {

        private static final String ADDRESS = "http://localhost:8080";
        private static final String TITLE = "F1+X: Laundry POS Application";

        public static Scene scene;
        public static VBox root;

        private Stage primaryStage;

        @Override
        public void start(Stage primaryStage) throws Exception {
                this.primaryStage = primaryStage;
                root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/com/app/f1xdesktop/layout.fxml")));

                Font.loadFont(Objects.requireNonNull(getClass().getResource("/static/fonts/Bungee-Regular.ttf")).toExternalForm(), 12);

                scene = new Scene(root, 800, 600);
                scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/static/css/styles.css")).toExternalForm());

                primaryStage.setTitle(TITLE);
                primaryStage.setMaximized(true);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(scene);
                primaryStage.getScene().setFill(Color.color(0.07058823529411765, 0.07058823529411765, 0.0784313725490196));

                Screen screen = Screen.getPrimary();
                Rectangle2D bounds = screen.getVisualBounds();
                root.setScaleX(800 / bounds.getWidth());
                root.setScaleY(600 / bounds.getHeight());

                primaryStage.show();

                restoreStageState();
                addMinimizedListener();
        }

        private void addMinimizedListener() {
                if (primaryStage == null) { return; }
                primaryStage.iconifiedProperty().addListener(new ChangeListener<Boolean>() {
                        @Override
                        public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                                if (newValue) { return; }
                                restoreStageState();
                        }
                });
        }

        private void restoreStageState() {
                if (primaryStage == null || root == null) { return; }
                ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.15), root);
                scaleTransition.setToX(1.0);
                scaleTransition.setToY(1.0);

                FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.15), root);
                fadeTransition.setToValue(1.0);

                ParallelTransition parallelTransition = new ParallelTransition(scaleTransition, fadeTransition);
                parallelTransition.play();
        }

        public static void main(String[] args) {
                launch(args);
        }

}
