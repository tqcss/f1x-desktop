package com.app.f1xdesktop.utils;

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;

public class Constants {

    // Application Settings
    public static final String ADDRESS = "http://localhost:8080";
    public static final String TITLE = "F1+X: Laundry POS Application";

    // Default Window Size
    public static final double WIDTH = 800;
    public static final double HEIGHT = 600;

    // Style and Resources
    public static final String FONT_PATH = "/static/fonts/Bungee-Regular.ttf";
    public static final String CSS_PATH = "/static/css/styles.css";

    // Image Resources
    public static final String FULLSCREEN_ICON_PATH = "/static/images/fullscreen_icon.png";
    public static final String WINDOW_ICON_PATH = "/static/images/window_icon.png";

    // Background Color
    public static final String BACKGROUND_COLOR = "#121214";

    // Screen Bounds
    public static final Rectangle2D SCREEN_BOUNDS = Screen.getPrimary().getVisualBounds();

}
