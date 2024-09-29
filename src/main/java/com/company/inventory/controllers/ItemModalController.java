package com.company.inventory.controllers;

import com.company.inventory.models.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;


public class ItemModalController {

    private InventoryController inventoryController;

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

        String name = itemName.getText();
        double stock = Double.parseDouble(stockQuantity.getText());
        double reorder = Double.parseDouble(reorderLevel.getText());
        String unit = unitMeasureChoices.getValue();

        Item newItem = new Item(name, unit, stock, reorder);
        newItem.save();

        inventoryController.refreshList();

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();

        System.out.println(newItem.getName() + " was added to the inventory with a ID of " + newItem.getId());

    }

    public void setInventoryController(InventoryController inventoryController) {
        this.inventoryController = inventoryController;
    }
}
