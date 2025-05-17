package com.app.f1xdesktop.utils;

import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class UIUtils {

    private static final double FADE_DURATION = 0.15;
    private static final double SCALE_TO_X = 0.7;
    private static final double SCALE_TO_Y = 0.7;

    /**
     * Restores the visual state of the stage (e.g., after minimizing).
     * @param stage The main application stage.
     * @param root  The root node of the scene.
     */
    public static void restoreStageState(Stage stage, VBox root) {
        if (stage == null || root == null) return;

        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(FADE_DURATION), root);
        fadeTransition.setToValue(1.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(FADE_DURATION), root);
        scaleTransition.setToX(1.0);
        scaleTransition.setToY(1.0);

        ParallelTransition parallelTransition = new ParallelTransition(fadeTransition, scaleTransition);
        parallelTransition.play();
    }

    /**
     * Creates a transition used before minimizing or closing a window.
     * @param root The root node to animate.
     * @return The composed transition.
     */
    public static ParallelTransition getCloseTransition(Node root) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(FADE_DURATION), root);
        fadeTransition.setFromValue(1.0);
        fadeTransition.setToValue(0.0);

        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(FADE_DURATION), root);
        scaleTransition.setFromX(1.0);
        scaleTransition.setFromY(1.0);
        scaleTransition.setToX(SCALE_TO_X);
        scaleTransition.setToY(SCALE_TO_Y);

        return new ParallelTransition(fadeTransition, scaleTransition);
    }
}
