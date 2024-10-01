package com.company.inventory.controllers;

import com.company.inventory.factory.ProdItemListCell;
import com.company.inventory.factory.ProdUsedItemListCell;
import com.company.inventory.models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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

    public void addItemToProduct(Item item) {
        if(!usedItems.contains(item)) {
            usedItems.add(item);
            System.out.println(item.getName() + "added to product");
        }
    }

    public void removeItemFromList(Item item) {
        if(items.contains(item)) {
            items.remove(item);
            System.out.println(item.getName() + " removed from list");
        }
    }

    public void addItemToList(Item item) {
        if(!items.contains(item)) {
            items.add(item);
        }
    }

    public void removeItemFromProduct(Item item) {
        if(usedItems.contains(item)) {
            usedItems.remove(item);
        }
    }

    public void confirmAddProduct(ActionEvent actionEvent) {
        String name = productNameField.getText();
        double price = Double.parseDouble(productPriceField.getText());

        Product product = new Product(name, price);
        product.save();

        for (Item item : usedItems) {

            ProdUsedItemListCell cell =  (ProdUsedItemListCell) usedItemList.lookup(".cell");
            if (cell != null) {
                double neededQuantity = Double.parseDouble(cell.getQuantity().getText());
                product.addProductIngredient(item, neededQuantity);
            }
        }

        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();

        resetList();
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
