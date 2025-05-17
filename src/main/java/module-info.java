module com.app.f1xdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;


    opens com.app.f1xdesktop to javafx.fxml;
    exports com.app.f1xdesktop;
    exports com.app.f1xdesktop.utils;
    opens com.app.f1xdesktop.utils to javafx.fxml;
    exports com.app.f1xdesktop.controller;
    opens com.app.f1xdesktop.controller to javafx.fxml;
}