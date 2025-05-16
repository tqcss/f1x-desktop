package com.app.f1xdesktop.subcomponents;

import com.app.f1xdesktop.Styles;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.InputStream;

public class TitleBarButton {

    private final Button button;

    private String imageName;
    private double height;
    private double width;
    private String defaultStyle;
    private String mouseEnterStyle;
    private String mouseExitStyle;

    public TitleBarButton() {
        this.button = new Button();
    }

    public Button build() {
        String imagePath = String.format("/static/images/%s", this.imageName);
        InputStream inputStream = getClass().getResourceAsStream(imagePath);

        if (inputStream == null) {
            System.err.printf("Couldn't find image: %s%n", imagePath);
            System.exit(1);
        }

        Image buttonImage = new Image(inputStream);
        ImageView buttonImageView = new ImageView(buttonImage);
        buttonImageView.setFitHeight(this.height);
        buttonImageView.setFitWidth(this.width);

        button.setCursor(Cursor.HAND);
        button.setGraphic(buttonImageView);
        button.setStyle(defaultStyle);

        button.setOnMouseEntered(_ -> button.setStyle(mouseEnterStyle));
        button.setOnMouseExited(_ -> button.setStyle(mouseExitStyle));

        return button;
    }

    public void setOnAction(EventHandler<ActionEvent> event) {
        button.setOnAction(event);
    }

    public void setImageName(String imageName) { this.imageName = imageName; }
    public void setHeight(double height) { this.height = height; }
    public void setWidth(double width) { this.width = width; }
    public void setDefaultStyle(String defaultStyle) { this.defaultStyle = defaultStyle; }
    public void setMouseEnterStyle(String mouseEnterStyle) { this.mouseEnterStyle = mouseEnterStyle; }
    public void setMouseExitStyle(String mouseExitStyle) { this.mouseExitStyle = mouseExitStyle; }

}
