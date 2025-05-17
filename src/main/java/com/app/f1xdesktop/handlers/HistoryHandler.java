package com.app.f1xdesktop.handlers;

import com.app.f1xdesktop.utils.Constants;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;

import java.time.LocalDateTime;

public class HistoryHandler {

    private final WebEngine webEngine;
    private final WebHistory history;
    private final Button navNext, navPrev;

    public HistoryHandler(WebEngine webEngine, WebHistory history, Button navNext, Button navPrev) {
        this.webEngine = webEngine;
        this.history = history;
        this.navNext = navNext;
        this.navPrev = navPrev;
    }

    public WebHistory getHistory() { return history; }

    public void addHistoryListeners() {
        System.out.println("listening");
        history.currentIndexProperty().addListener((observable, oldValue, newValue) -> {
            int currentIndex = newValue.intValue();

            if (currentIndex >= 0 && currentIndex < history.getEntries().size()) {
                String lastUrl = history.getEntries().get(currentIndex - 1).getUrl();
                String currentUrl = history.getEntries().get(currentIndex).getUrl();

                System.out.printf("%s | HistoryHandler: Add '%s' to history stack.%n", LocalDateTime.now(), currentUrl);
                if (lastUrl != null && lastUrl.endsWith(Constants.FALLBACK_UI_PATH)) { return; }
            }
            navPrev.setDisable(currentIndex == 0);
            navNext.setDisable(currentIndex >= history.getEntries().size() - 1);
        });
    }

    public void reloadCurrentPage() {
        String currentUrl = webEngine.getLocation();
        webEngine.load(currentUrl);
    }

    public void waitFor(BooleanProperty connectedStatus) {
        ChangeListener<Boolean> connectionListener = new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (!newValue) {
                    System.err.println("Unexpected error occurred whilst establishing server connection");
                    return;
                }
                addHistoryListeners();
                connectedStatus.removeListener(this);
            }
        };
        connectedStatus.addListener(connectionListener);
    }

}
