package com.company.inventory.controllers;

import com.company.inventory.factories.ProdItemListCell;
import com.company.inventory.factories.ProdUsedItemListCell;
import com.company.inventory.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class AddProductController {

    @FXML private Button btnConfirm;
    @FXML private TextField productPriceField;
    @FXML private TextField productNameField;
    @FXML private Button btnCancel;
    @FXML private ListView<Item> itemList;
    @FXML private ListView<Item> usedItemList;
    private ObservableList<Item> items;
    private ObservableList<Item> usedItems;
    private ObservableList<Item> copyItems;
    private Map<Item, Double> itemQuantities = new HashMap<>();
    @FXML
    public void initialize() {
        items = Database.getItemList();
        copyItems = FXCollections.observableArrayList(items);
        itemList.setItems(items);

        usedItems = FXCollections.observableArrayList();
        usedItemList.setItems(usedItems);

        itemList.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        itemList.setCellFactory(param -> new ProdItemListCell(this));

        usedItemList.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        usedItemList.setCellFactory(param -> new ProdUsedItemListCell(this));

        Platform.runLater(() -> {
            Stage stage = (Stage) btnCancel.getScene().getWindow();
            stage.setOnCloseRequest(event -> {
                resetList();
            });
        });
    }
    public void confirmAddProduct(ActionEvent actionEvent) {
        String name = productNameField.getText().trim(); // Trim whitespace
        String priceText = productPriceField.getText().trim(); // Trim whitespace

        // Check for empty fields
        if (name.isEmpty()) {
            showError("Product name cannot be empty.");
            return; // Early exit if name is empty
        }

        if (priceText.isEmpty()) {
            showError("Product price cannot be empty.");
            return; // Early exit if price is empty
        }

        // Parse price and handle potential number format exceptions
        double price;
        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            showError("Please enter a valid price.");
            return; // Early exit if price is not a valid number
        }

        for (Item item : usedItems) {
            double neededQuantity = itemQuantities.get(item);
            if (neededQuantity <= 0) {
                showError("Quantity for item " + item.getName() + " must be greater than 0.");
                return; // Early exit if any item's quantity is 0 or negative
            }
        }


        // Create the product if all checks pass
        Product product = new Product(name, price);
        product.save();
        Database.addProductToList(product);

        for (Item item : usedItems) {
            double neededQuantity = itemQuantities.get(item);
            product.addProductIngredient(item, neededQuantity);
        }

        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();

        resetList();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Input Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void addItemToProduct(Item item, double quantity) {
        if(!usedItems.contains(item)) {
            usedItems.add(item);
            itemQuantities.put(item, quantity);
            System.out.println(item.getName() + "added to product");
        }
    }

    public void removeItemFromProduct(Item item) {
        if(usedItems.contains(item)) {
            usedItems.remove(item);
            itemQuantities.remove(item);
        }
    }

    public void updateItemQuantity(Item item, double quantity) {
        itemQuantities.put(item, quantity);
    }

    public void cancel(ActionEvent actionEvent) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();

        resetList();
    }

    private void resetList() {
        items.clear();
        items.addAll(copyItems);
        usedItems.clear();
    }

}
