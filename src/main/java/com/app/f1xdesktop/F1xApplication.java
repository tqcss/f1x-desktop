package com.app.f1xdesktop;

import com.app.f1xdesktop.components.ContentDisplay;
import com.app.f1xdesktop.components.TitleBar;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class F1xApplication extends Application {

        private static final String ADDRESS = "http://localhost:8080";
        private static final String TITLE = "F1+X Desktop:";

        @Override
        public void start(Stage primaryStage) {
                ContentDisplay contentDisplay = new ContentDisplay();
                TitleBar titleBar = new TitleBar(primaryStage);
                titleBar.setTitle(TITLE);
                titleBar.setHeight(45);
                titleBar.setWidth(Double.MAX_VALUE);
                titleBar.setAlignment(Pos.CENTER_LEFT);

                Handlers.ContentHandler.load(ADDRESS);
                Handlers.DisplayHandler.display(
                        titleBar.build(),
                        contentDisplay.build()
                );

                primaryStage.setMaximized(true);
                primaryStage.setTitle(TITLE);
                primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setScene(Handlers.DisplayHandler.getRootScene());

                primaryStage.show();
        }

        public static void main(String[] args) {
                launch(args);
        }

}
