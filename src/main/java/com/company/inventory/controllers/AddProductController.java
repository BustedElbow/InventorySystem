package com.company.inventory.controllers;

import com.company.inventory.models.Database;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class AddProductController {

    @FXML
    private ListView<String> itemList;

    private ObservableList<String> items;

    @FXML
    public void initialize() {
        items = Database.getItemList();
        itemList.setItems(items);
    }
}
