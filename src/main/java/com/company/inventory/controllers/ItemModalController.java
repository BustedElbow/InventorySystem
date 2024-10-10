package com.company.inventory.controllers;

import com.company.inventory.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ItemModalController {

    @FXML private ChoiceBox<String> unitMeasureChoices;
    @FXML private TextField itemName;
    @FXML private TextField stockQuantity;
    @FXML private TextField reorderLevel;
    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;

    @FXML
    private void initialize() {
        unitMeasureChoices.getItems().addAll("Kilograms", "Liters", "Pieces");
        unitMeasureChoices.setValue("Kilograms");
    }

    @FXML
    private void btnCancel(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnConfirm(ActionEvent event) {
        try {
            // Item name validation
            if (itemName.getText().isEmpty()) {
                showErrorModal("Item name cannot be empty.");
                return;
            }

            // Stock quantity validation
            String stockText = stockQuantity.getText();
            if (stockText.isEmpty()) {
                showErrorModal("Stock quantity cannot be empty.");
                return;
            }
            double stock = Double.parseDouble(stockText);
            if (stock < 0) {
                showErrorModal("Stock quantity must be a non-negative number.");
                return;
            }

            // Reorder level validation
            String reorderText = reorderLevel.getText();
            if (reorderText.isEmpty()) {
                showErrorModal("Reorder level cannot be empty.");
                return;
            }
            double reorder = Double.parseDouble(reorderText);
            if (reorder < 0) {
                showErrorModal("Reorder level must be a non-negative number.");
                return;
            }

            // Get the unit of measure from the choice box
            String unit = unitMeasureChoices.getValue();

            // Creating and saving the new item
            Item newItem = new Item(itemName.getText(), unit, stock, reorder);
            newItem.save();

            // Adding the new item to the database list
            Database.addItemToList(newItem);

            // Closing the modal window
            Stage stage = (Stage) confirmBtn.getScene().getWindow();
            stage.close();

            System.out.println(newItem.getName() + " was added to the inventory with an ID of " + newItem.getId());

        } catch (NumberFormatException e) {
            // Specific error for stock or reorder fields containing invalid numeric input
            showErrorModal("Please enter valid numeric values for stock and reorder level.");
        }
    }

    private void showErrorModal(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
