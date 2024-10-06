package com.company.inventory.controllers;

import com.company.inventory.models.Item;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.Optional;

public class EditItemController {
    @FXML private Button saveBtn;
    @FXML private TextField itemName;
    @FXML private Button cancelBtn;
    @FXML private TextField stockQuantity;
    @FXML private TextField reorderLevel;
    @FXML
    private ChoiceBox<String> unitMeasureChoices;
    @FXML private Button deleteBtn;
    private Item item;

    @FXML
    private void initialize() {
        unitMeasureChoices.getItems().addAll("Kilograms", "Liters", "Pieces");
    }

    public void btnSave(ActionEvent actionEvent) {
        String name = itemName.getText();
        String stockText = stockQuantity.getText();
        String reorderText = reorderLevel.getText();
        String unitMeasure = unitMeasureChoices.getValue();

        // Check if item name is empty
        if (name.isEmpty()) {
            showErrorDialog("Item name cannot be empty.");
            return;
        }

        // Check if stock quantity is empty
        if (stockText.isEmpty()) {
            showErrorDialog("Stock quantity cannot be empty.");
            return;
        }

        // Check if reorder level is empty
        if (reorderText.isEmpty()) {
            showErrorDialog("Reorder level cannot be empty.");
            return;
        }

        // Check if stock quantity is a valid double
        double stock;
        try {
            stock = Double.parseDouble(stockText);
            if (stock < 0) {
                showErrorDialog("Stock quantity cannot be a negative number.");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Stock quantity must be a valid number.");
            return;
        }

        // Check if reorder level is a valid double
        double reorderLevelValue;
        try {
            reorderLevelValue = Double.parseDouble(reorderText);
            if (reorderLevelValue < 0) {
                showErrorDialog("Reorder level cannot be a negative number.");
                return;
            }
        } catch (NumberFormatException e) {
            showErrorDialog("Reorder level must be a valid number.");
            return;
        }

        // Set item details
        item.setName(name);
        item.setStock(stock);
        item.setReorderLevel(reorderLevelValue);
        item.setUnitMeasure(unitMeasure);

        item.update();

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    private void showErrorDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void btnDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Are you sure you want to delete this item?");
        alert.setContentText("Item Name: " + item.getName());

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            item.delete();

            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();

            InventoryController.getInstance().refreshItemList();
            InventoryArchiveController.getInstance().refreshArcItemList();
        }
    }

    public void setItem(Item item) {
        this.item = item;
        itemName.setText(item.getName());
        stockQuantity.setText(Double.toString(item.getStock()));
        reorderLevel.setText(Double.toString(item.getReorderLevel()));
        unitMeasureChoices.setValue(item.getUnitMeasure());
    }
}
