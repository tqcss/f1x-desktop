package com.app.f1xdesktop;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class Handlers {

    public static class ContentHandler {
        private static final WebView webView = new WebView();
        private static final WebEngine webEngine = webView.getEngine();

        public static void load(String address) { webEngine.load(address); }
        public static WebView getContent() { return webView; }
    }

    public static class DisplayHandler {
        private static Scene rootScene;

        public static void display(Node... components) {
            VBox displayContainer = new VBox(components);

            for (Node component : components) {
                if (!(component instanceof WebView)) { continue; }
                VBox.setVgrow(component, Priority.ALWAYS);
                break;
            }

            rootScene = new Scene(displayContainer);
            rootScene.setFill(Color.color(0.07058823529411765, 0.07058823529411765, 0.0784313725490196));
        }

        public static Scene getRootScene() { return rootScene; }

    }

}
