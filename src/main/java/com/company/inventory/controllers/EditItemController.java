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
        item.setName(itemName.getText());
        item.setStock(Double.parseDouble(stockQuantity.getText()));
        item.setReorderLevel(Double.parseDouble(reorderLevel.getText()));
        item.setUnitMeasure(unitMeasureChoices.getValue().toString());

        item.update();

        Stage stage = (Stage) saveBtn.getScene().getWindow();
        stage.close();
    }

    public void btnCancel(ActionEvent actionEvent) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    public void btnDelete(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Item");
        alert.setContentText("Are you sure you want to delete this item: " + item.getName() + "?");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            item.delete();

            Stage stage = (Stage) deleteBtn.getScene().getWindow();
            stage.close();

            InventoryController.getInstance().refreshItemList();
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
