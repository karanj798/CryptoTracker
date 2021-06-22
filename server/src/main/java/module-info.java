module com.assignment {
    requires javafx.controls;
    requires org.json;
    requires com.google.gson;
    requires javafx.fxml;
    requires org.apache.commons.io;

    opens com.assignment to javafx.fxml;

    exports com.assignment;
    exports com.assignment.models;
    exports com.assignment.utils;
}