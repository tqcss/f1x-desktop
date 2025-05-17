package com.app.f1xdesktop;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LayoutController implements Initializable {

    @FXML
    private VBox root;

    @FXML
    public BorderPane titleBar;

    public Label title;
    public Button minimize;
    public Button maximize;
    public Button close;

    @FXML
    private ImageView maximizeImageView;

    @FXML
    private Image maximizeImage;

    @FXML
    private WebView contentView;

    private final Screen primaryScreen = Screen.getPrimary();
    private final Rectangle2D screenBounds = primaryScreen.getVisualBounds();

    private boolean isMaximized = true;

    private Image fullScreenImage;
    private Image windowedImage;

    private Scene scene;
    private Stage stage;

    private double xDragOffset;
    private double yDragOffset;
    private double currentWindowSizeX = 800;
    private double currentWindowSizeY = 600;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = contentView.getEngine();
        VBox.setVgrow(contentView, Priority.ALWAYS);
        webEngine.load(Constants.getAddress());
    }

    private Scene getScene() {
        if (scene != null) { return scene; }
        scene = root.getScene();
        return scene;
    };

    private Stage getStage() {
        if (stage != null) { return stage; }
        stage = (Stage)getScene().getWindow();
        return stage;
    }

    private Image getFullscreenImage() {
        if (fullScreenImage != null) { return fullScreenImage; }
        fullScreenImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/fullscreen_icon.png")));
        return fullScreenImage;
    }
    private Image getWindowedImage() {
        if (windowedImage != null) { return windowedImage; }
        windowedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/static/images/window_icon.png")));
        return windowedImage;
    }

    @FXML
    private void close(ActionEvent event) {
//        if (isMaximized) {
//            getStage().setMaximized(false);
//            Screen primaryScreen = Screen.getPrimary();
//            Rectangle2D screenBounds = primaryScreen.getVisualBounds();
//            stage.setHeight(screenBounds.getHeight());
//            stage.setWidth(screenBounds.getWidth());
//            stage.setX(0);
//            stage.setY(0);
//        }

        ParallelTransition parallelTransition = getParallelTransition();
        parallelTransition.setOnFinished(_ -> getStage().close());
        parallelTransition.play();
    }

    private ParallelTransition getParallelTransition() {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(0.15), root);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);
//        fadeTransition.setOnFinished(_ -> getStage().close());

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.15), root);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(0.7);
        scaleTransition.setToY(0.7);

        return new ParallelTransition(fadeTransition, scaleTransition);
    }

    @FXML
    private void toggleMaximize(ActionEvent event) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.1), root);
        scaleTransition.setFromX(currentWindowSizeX);
        scaleTransition.setFromY(currentWindowSizeY);

        if (!isMaximized) {
            isMaximized = true;
            getStage().setMaximized(true);
            maximizeImageView.setImage(getWindowedImage());

            scaleTransition.setFromX(currentWindowSizeX / screenBounds.getWidth());
            scaleTransition.setFromY(currentWindowSizeY / screenBounds.getHeight());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
        } else {
            isMaximized = false;
            maximizeImageView.setImage(getFullscreenImage());

            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(currentWindowSizeX / screenBounds.getWidth());
            scaleTransition.setToY(currentWindowSizeY / screenBounds.getHeight());

            scaleTransition.setOnFinished(_ -> {
                root.setScaleX(1.0);
                root.setScaleY(1.0);
                getStage().setMaximized(false);
            });
        }

        scaleTransition.play();
    }

    @FXML
    private void onTitlebarPressed(MouseEvent event) {
        if (isMaximized) { return; }
        xDragOffset = event.getSceneX();
        yDragOffset = event.getSceneY();
    }

    @FXML
    private void onTitlebarDrag(MouseEvent event) {
        if (isMaximized) { return; }
        getStage().setX(event.getScreenX() - xDragOffset);
        getStage().setY(event.getScreenY() - yDragOffset);
    }

    @FXML
    private void minimize() {

        ParallelTransition parallelTransition = getParallelTransition();
        parallelTransition.setOnFinished(_ -> getStage().setIconified(true));
        parallelTransition.play();
    }

}
