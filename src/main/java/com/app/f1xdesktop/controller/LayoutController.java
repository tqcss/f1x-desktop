package com.app.f1xdesktop.controller;

import com.app.f1xdesktop.handlers.ConnectionHandler;
import com.app.f1xdesktop.handlers.HistoryHandler;
import com.app.f1xdesktop.utils.Constants;
import com.app.f1xdesktop.utils.UIUtils;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Rectangle2D;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.ScheduledExecutorService;

public class LayoutController implements Initializable {

    @FXML private VBox root;
    @FXML public BorderPane titleBar;
    @FXML public Label title;
    @FXML public Button minimize, maximize, close, navPrev, navNext;
    @FXML private ImageView maximizeImageView;
    @FXML private WebView contentView;

    private boolean isMaximized = true;
    private double xDragOffset, yDragOffset;

    private ConnectionHandler connectionHandler;
    private HistoryHandler historyHandler;
    private Image fullScreenImage, windowedImage;

    private final Rectangle2D screenBounds = Constants.SCREEN_BOUNDS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        WebEngine webEngine = contentView.getEngine();
        BooleanProperty connectedStatus = new SimpleBooleanProperty(false);
        connectionHandler = new ConnectionHandler(webEngine, connectedStatus);
        historyHandler = new HistoryHandler(webEngine, webEngine.getHistory(), navNext, navPrev);

        VBox.setVgrow(contentView, Priority.ALWAYS);
        connectionHandler.loadWebViewContent();

        if (connectedStatus.get()) {
            historyHandler.addHistoryListeners();
        } else {
            historyHandler.waitFor(connectedStatus);
        }
    }

    @FXML private void close() {
        ParallelTransition transition = UIUtils.getCloseTransition(root);
        transition.setOnFinished(e -> {
            ScheduledExecutorService scheduler = connectionHandler.getScheduler();
            if (scheduler != null) { scheduler.shutdownNow(); }
            getStage().close();
        });
        transition.play();
    }

    @FXML private void minimize() {
        ParallelTransition transition = UIUtils.getCloseTransition(root);
        transition.setOnFinished(e -> getStage().setIconified(true));
        transition.play();
    }

    @FXML private void toggleMaximize() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.1), root);

        if (!isMaximized) {
            isMaximized = true;
            getStage().setMaximized(true);
            maximizeImageView.setImage(getWindowedImage());

            scaleTransition.setFromX(Constants.WIDTH / screenBounds.getWidth());
            scaleTransition.setFromY(Constants.HEIGHT / screenBounds.getHeight());
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
        } else {
            isMaximized = false;
            maximizeImageView.setImage(getFullscreenImage());

            scaleTransition.setFromX(1.0);
            scaleTransition.setFromY(1.0);
            scaleTransition.setToX(Constants.WIDTH / screenBounds.getWidth());
            scaleTransition.setToY(Constants.HEIGHT / screenBounds.getHeight());

            scaleTransition.setOnFinished(_ -> {
                root.setScaleX(1.0);
                root.setScaleY(1.0);
                getStage().setMaximized(false);
            });
        }

        scaleTransition.play();
    }

    @FXML private void onTitlebarPressed(MouseEvent e) {
        if (isMaximized) { return; }
        xDragOffset = e.getSceneX();
        yDragOffset = e.getSceneY();
    }

    @FXML private void onTitlebarDrag(MouseEvent e) {
        if (isMaximized) { return; }
        getStage().setX(e.getScreenX() - xDragOffset);
        getStage().setY(e.getScreenY() - yDragOffset);
    }

    @FXML private void navigateNext() {
        WebHistory history = historyHandler.getHistory();

        if (history.getCurrentIndex() >= history.getEntries().size() - 1) { return; }
        history.go(1);
        // historyHandler.reloadCurrentPage();
    }

    @FXML private void navigatePrev() {
        WebHistory history = historyHandler.getHistory();

        if (history.getCurrentIndex() <= 0) { return; }
        history.go(-1);
        // historyHandler.reloadCurrentPage();
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    private Image getFullscreenImage() {
        if (fullScreenImage != null) { return fullScreenImage; }
        fullScreenImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.FULLSCREEN_ICON_PATH)));
        return fullScreenImage;
    }

    private Image getWindowedImage() {
        if (windowedImage != null) { return windowedImage; }
        windowedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.WINDOW_ICON_PATH)));
        return windowedImage;
    }
}
