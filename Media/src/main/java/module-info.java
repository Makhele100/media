module com.example.media {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.desktop;


    opens com.example.media to javafx.fxml;
    exports com.example.media;
}