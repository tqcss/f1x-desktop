package com.app.f1xdesktop.handlers;

import com.app.f1xdesktop.utils.Constants;
import javafx.application.Platform;
import javafx.scene.web.WebEngine;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ConnectionHandler {

    private ScheduledExecutorService scheduler;
    private final WebEngine webEngine;

    public boolean connectionEstablished = false;

    public ConnectionHandler(WebEngine webEngine) {
        this.webEngine = webEngine;
    }

    public ScheduledExecutorService getScheduler() { return scheduler; }

    public void loadWebViewContent() {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(Constants.ADDRESS).openConnection();
            connection.setConnectTimeout(1500);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                webEngine.load(Constants.ADDRESS);
                connectionEstablished = true;
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
                connection.setConnectTimeout(1500);
                connection.connect();

                if (connection.getResponseCode() == 200) {
                    Platform.runLater(() -> webEngine.load(Constants.ADDRESS));
                    scheduler.shutdown(); // stop polling once successful
                    connectionEstablished = true;
                    // addHistoryListeners();
                }
            } catch (IOException ignored) {
                // still offline, keep polling
            }
        }, Constants.POLLING_DELAY, Constants.POLLING_PERIOD, TimeUnit.SECONDS);
    }

}
