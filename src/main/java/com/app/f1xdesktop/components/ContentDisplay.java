package com.app.f1xdesktop.components;

import com.app.f1xdesktop.Handlers;
import javafx.scene.web.WebView;

public class ContentDisplay {

    public WebView build() { return Handlers.ContentHandler.getContent(); }

}
