module com.example.hiarflow {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires java.naming;
    requires java.sql;
    requires org.hibernate.orm.core;
    opens com.example.hiarflow.controller to javafx.fxml;
    opens com.example.hiarflow.model to org.hibernate.orm.core, javafx.base;
    opens com.example.hiarflow to javafx.fxml;
    exports com.example.hiarflow;
}