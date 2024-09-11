module com.example.inventorysystem {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.inventorysystem to javafx.fxml;
    exports com.example.inventorysystem;
}