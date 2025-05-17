package com.app.f1xdesktop.handlers;

import com.app.f1xdesktop.utils.Constants;
import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.scene.web.WebEngine;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler {

    private final BooleanProperty connectedStatus;
    private ScheduledExecutorService scheduler;
    private final WebEngine webEngine;

    public ConnectionHandler(WebEngine webEngine, BooleanProperty connectedStatus) {
        this.webEngine = webEngine;
        this.connectedStatus = connectedStatus;
    }

    public ScheduledExecutorService getScheduler() { return scheduler; }

    public void loadWebViewContent() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ADDRESS).openConnection();
            connection.setConnectTimeout(1500);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                webEngine.load(Constants.ADDRESS);
                connectedStatus.set(true);
                return;
                // addHistoryListeners();
            } else {
                loadFallback();
            }
        } catch (IOException e) {
            loadFallback();
        }

        // start polling in background to check for remote availability
        pollForRemoteServer();
    }

    private void loadFallback() {
        URL fallbackUrl = getClass().getResource(Constants.FALLBACK_UI_PATH);
        if (fallbackUrl != null) {
            webEngine.load(fallbackUrl.toExternalForm());
        } else {
            webEngine.loadContent("""
                    <h1>Offline Mode</h1>
                    <p>Unable to load fallback page.</p>
                    """
            );
        }
    }

    private void pollForRemoteServer() {
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ADDRESS).openConnection();
                connection.setConnectTimeout(Constants.CONNECTION_TIMEOUT_MS);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    Platform.runLater(() -> webEngine.load(Constants.ADDRESS));
                    scheduler.shutdown(); // stop polling once successful
                    connectedStatus.set(true);
                    // addHistoryListeners();
                }
            } catch (IOException ignored) {
                // still offline, keep polling
            }
        }, Constants.POLLING_DELAY_SEC, Constants.POLLING_PERIOD_SEC, TimeUnit.SECONDS);
    }

}
