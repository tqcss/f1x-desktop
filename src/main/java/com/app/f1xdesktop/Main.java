package com.app.f1xdesktop;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.stage.Screen;
import javafx.geometry.Rectangle2D;

public class Main extends Application {
        @Override
        public void start(Stage primaryStage) {
                WebView webView = new WebView();
                webView.getEngine().load("http://localhost:8080/home"); // Load the initial website

                StackPane root = new StackPane(webView);
                Scene scene = new Scene(root); // Create scene without initial dimensions

                primaryStage.setTitle("F1+X Desktop Application");
                primaryStage.setScene(scene);

                // Get the primary screen's visual bounds
                Screen primaryScreen = Screen.getPrimary();
                Rectangle2D bounds = primaryScreen.getVisualBounds();

                // Set the stage to full screen based on the screen bounds
                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(bounds.getWidth());
                primaryStage.setHeight(bounds.getHeight());

                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}