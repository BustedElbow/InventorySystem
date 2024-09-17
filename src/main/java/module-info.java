module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.company.inventory.controllers to javafx.fxml;
    exports com.company.inventory;
}