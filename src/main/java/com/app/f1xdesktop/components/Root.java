package com.app.f1xdesktop.components;

import javafx.scene.Node;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

public class Root extends VBox {

    public Root(Node... nodes) {
        super(nodes);

        for (Node node : nodes) {
            if (!(node instanceof WebView)) { continue; }
            VBox.setVgrow(node, Priority.ALWAYS);
            break;
        }
    }

}
