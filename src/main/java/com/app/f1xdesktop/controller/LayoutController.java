package com.app.f1xdesktop.controller;

import com.app.f1xdesktop.utils.Constants;
import com.app.f1xdesktop.utils.UIUtils;
import javafx.animation.ParallelTransition;
import javafx.animation.ScaleTransition;
import javafx.application.Platform;
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

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class LayoutController implements Initializable {

    @FXML private VBox root;
    @FXML public BorderPane titleBar;
    @FXML public Label title;
    @FXML public Button minimize, maximize, close, navPrev, navNext;
    @FXML private ImageView maximizeImageView;
    @FXML private WebView contentView;

    private boolean isMaximized = true;
    private double xDragOffset, yDragOffset;
    private double currentWindowSizeX = Constants.WIDTH;
    private double currentWindowSizeY = Constants.HEIGHT;

    private ScheduledExecutorService scheduler;
    private Image fullScreenImage, windowedImage;
    private WebEngine webEngine;
    private WebHistory history;

    private final Rectangle2D screenBounds = Constants.SCREEN_BOUNDS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        webEngine = contentView.getEngine();
        VBox.setVgrow(contentView, Priority.ALWAYS);

        loadWebViewContent();
        history = webEngine.getHistory();
    }

    private void loadWebViewContent() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ADDRESS).openConnection();
            connection.setConnectTimeout(1500); // 1.5 second timeout
            connection.connect();

            if (connection.getResponseCode() == 200) {
                webEngine.load(Constants.ADDRESS);
            } else {
                loadFallback();
            }
        } catch (IOException e) {
            loadFallback();
        }

        // Start polling in background to check for remote availability
        pollForRemoteServer();
    }

    private void loadFallback() {
        URL fallbackUrl = getClass().getResource("/com/app/f1xdesktop/fallback.html");
        if (fallbackUrl != null) {
            webEngine.load(fallbackUrl.toExternalForm());
        } else {
            webEngine.loadContent("<h1>Offline Mode</h1><p>Unable to load fallback page.</p>");
        }
    }

    private void pollForRemoteServer() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ADDRESS).openConnection();
                connection.setConnectTimeout(1500);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    Platform.runLater(() -> webEngine.load(Constants.ADDRESS));
                    scheduler.shutdown(); // stop polling once successful
                }
            } catch (IOException ignored) {
                // still offline, keep polling
            }
        }, 3, 5, TimeUnit.SECONDS); // initial delay 3s, check every 5s
    }

    @FXML private void close() {
        ParallelTransition transition = UIUtils.getCloseTransition(root);
        transition.setOnFinished(e -> {
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

    @FXML private void onTitlebarPressed(MouseEvent e) {
        if (!isMaximized) {
            xDragOffset = e.getSceneX();
            yDragOffset = e.getSceneY();
        }
    }

    @FXML private void onTitlebarDrag(MouseEvent e) {
        if (!isMaximized) {
            getStage().setX(e.getScreenX() - xDragOffset);
            getStage().setY(e.getScreenY() - yDragOffset);
        }
    }

    @FXML private void navigateNext() {
        if (history.getCurrentIndex() < history.getEntries().size() - 1) {
            history.go(1);
        }
    }

    @FXML private void navigatePrev() {
        if (history.getCurrentIndex() > 0) {
            history.go(-1);
        }
    }

    private void addHistoryListeners() {
        history.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            int currentIndex = newValue.intValue();
            navPrev.setDisable(currentIndex == 0);
            navNext.setDisable(currentIndex >= history.getEntries().size() - 1);
        });
    }

    private Stage getStage() {
        return (Stage) root.getScene().getWindow();
    }

    private Image getFullscreenImage() {
        if (fullScreenImage == null) {
            fullScreenImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.FULLSCREEN_ICON_PATH)));
        }
        return fullScreenImage;
    }

    private Image getWindowedImage() {
        if (windowedImage == null) {
            windowedImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream(Constants.WINDOW_ICON_PATH)));
        }
        return windowedImage;
    }
}
