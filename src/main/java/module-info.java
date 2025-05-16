module com.app.f1xdesktop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires java.desktop;


    opens com.app.f1xdesktop to javafx.fxml;
    exports com.app.f1xdesktop;
}