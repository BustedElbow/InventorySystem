package com.company.inventory.controllers;

import com.company.inventory.models.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class RestockController {
    @FXML private Button addBtn;
    @FXML private Button cancelBtn;
    @FXML private Label stockQuantityLabel;
    @FXML private TextField addStockField;

    private Item item;

    public void setItem(Item item) {
        this.item = item;
        stockQuantityLabel.setText("Current Stock: " + item.getStock());
    }

    public void addStock(ActionEvent actionEvent) {
        try {
            double quantityToAdd = Double.parseDouble(addStockField.getText());
            if (quantityToAdd > 0) {
                item.restock(quantityToAdd);

                ((Stage) addBtn.getScene().getWindow()).close();
            } else {

                showAlert("Please enter a valid quantity to add.");
            }
        } catch (NumberFormatException e) {
            showAlert("Invalid quantity format. Please enter a number.");
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        ((Stage) cancelBtn.getScene().getWindow()).close();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
