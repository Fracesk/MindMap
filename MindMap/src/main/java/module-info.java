module com.example.mindmap {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.swing;

    opens com.example.mindmap to javafx.fxml;
    exports com.example.mindmap;
}