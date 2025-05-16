package com.app.f1xdesktop;

public class Styles {

    public static String titleBar() {
        return """
                -fx-background-color: #121214;
                -fx-border-color: transparent transparent #1C1C1E transparent;
                -fx-border-width: 0 0 1 0;
                """;
    }

    public static String titleBarLabel() {
        return """
                -fx-text-fill: white;
                -fx-font-size: 14;
                """;
    }

    public static String defaultCloseButton() {
        return """
                "-fx-background-color: transparent;"
                """;
    }
    public static String hoveredCloseButton() {
        return """
                -fx-background-color: #29292D;
                """;
    }

}
