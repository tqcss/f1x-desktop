package com.app.f1xdesktop.components;

import com.app.f1xdesktop.Styles;
import com.app.f1xdesktop.subcomponents.TitleBarButton;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import javafx.util.Duration;

public class TitleBar {

    private final HBox hBox;
    private final Stage stage;
    private final ObservableList<Node> children;

    private String title;
    private double height;
    private double width;
    private Pos alignment;

    public TitleBar(Stage stage) {
        this.hBox = new HBox();
        this.stage = stage;
        this.children = hBox.getChildren();
    }

    public HBox build() {
        hBox.setPrefHeight(this.height);
        hBox.setPrefWidth(this.width);
        hBox.setAlignment(this.alignment);
        hBox.setStyle(Styles.titleBar());

        // add children
        children.add(createTitleLabel());
        children.add(createCloseButton());

        return hBox;
    }

    private Label createTitleLabel() {
        Label titleLabel = new Label(title);
        HBox.setHgrow(titleLabel, Priority.ALWAYS);
        titleLabel.setStyle(Styles.titleBarLabel());

        return titleLabel;
    }

    private Button createCloseButton() {
        TitleBarButton closeButton = new TitleBarButton();

        closeButton.setImageName("close-icon.png");
        closeButton.setHeight(25);
        closeButton.setWidth(25);
        closeButton.setDefaultStyle(Styles.defaultCloseButton());
        closeButton.setMouseEnterStyle(Styles.hoveredCloseButton());
        closeButton.setMouseExitStyle(Styles.defaultCloseButton());
        closeButton.setOnAction(_ -> {
            FadeTransition transition = new FadeTransition(Duration.millis(300), stage.getScene().getRoot());
            transition.setFromValue(1.0);
            transition.setToValue(0.0);
            transition.setOnFinished(_ -> stage.close());
            transition.play();
        });

        return closeButton.build();
    }

    public void setTitle(String title) { this.title = title; }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setAlignment(Pos alignment) { this.alignment = alignment; }

}
