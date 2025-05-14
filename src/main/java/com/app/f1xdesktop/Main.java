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
                webView.getEngine().load("http://localhost:8080/register");

                StackPane root = new StackPane(webView);
                Scene scene = new Scene(root);

                primaryStage.setTitle("F1+X: Laundry POS Application");
                primaryStage.setScene(scene);

                Screen primaryScreen = Screen.getPrimary();
                Rectangle2D bounds = primaryScreen.getVisualBounds();

                primaryStage.setX(bounds.getMinX());
                primaryStage.setY(bounds.getMinY());
                primaryStage.setWidth(800);
                primaryStage.setHeight(500);

                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }
}