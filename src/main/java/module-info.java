module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires de.jensd.fx.glyphs.fontawesome;


    opens com.company.inventory.controllers to javafx.fxml;
    exports com.company.inventory;
    exports com.company.inventory.controllers;
    exports com.company.inventory.models;
    exports com.company.inventory.views;
}