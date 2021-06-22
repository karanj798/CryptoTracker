module com.assignment {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.json;
    requires com.google.gson;

    opens com.assignment to javafx.fxml;
    exports com.assignment;
    exports com.assignment.models;
    exports com.assignment.utils;
}