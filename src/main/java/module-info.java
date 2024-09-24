module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;


    opens com.company.inventory.controllers to javafx.fxml;
    exports com.company.inventory;
    exports com.company.inventory.controllers;
    exports com.company.inventory.models;
}