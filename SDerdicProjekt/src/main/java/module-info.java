module hr.javafx.main {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires jakarta.json.bind;
    requires org.slf4j;
    requires java.xml;
    requires java.desktop;
    requires java.sql;
    requires com.h2database;
    requires jbcrypt;

    opens hr.javafx.main to javafx.fxml;
    exports hr.javafx.main;
    exports hr.javafx.controllers;
    opens hr.javafx.controllers to javafx.fxml;
    opens hr.javafx.model to jakarta.json.bind;
    exports hr.javafx.model;
    exports hr.javafx.exceptions;
    opens hr.javafx.exceptions to jakarta.json.bind;

}