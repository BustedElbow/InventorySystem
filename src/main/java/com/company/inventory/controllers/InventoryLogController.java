package com.company.inventory.controllers;

import com.company.inventory.models.Database;
import com.company.inventory.models.InventoryLog;
import com.company.inventory.models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

public class InventoryLogController {
    private static InventoryLogController instance;
    @FXML
    private ListView<InventoryLog> logListView;

    public InventoryLogController() {
        instance = this;
    }
    public void initialize() {
        refreshLogItemList();
    }

    public void refreshLogItemList() {
        Database.reloadInventoryLogsFromDatabase();
        ObservableList<InventoryLog> logs = Database.getInventoryLogs();
        logListView.setItems(logs);
    }

    public void btnAddItem(ActionEvent actionEvent) {
    }
    public static InventoryLogController getInstance() {
        return instance;
    }

    public void loadInventory(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventory();
    }

    public void loadArchive(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventoryArchive();
    }

}
