package com.company.inventory.controllers;

import com.company.inventory.factories.DashStockSumListCell;
import com.company.inventory.factories.InventoryListCell;
import com.company.inventory.factories.LowStockListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ListView;

public class DashStockSumController {
    private static DashStockSumController instance;
    public ListView quickStockSummaryLIst;

    public DashStockSumController() {
        instance = this;
    };

    public void initialize(){
        refreshItemList();

        quickStockSummaryLIst.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        quickStockSummaryLIst.setCellFactory(param -> new DashStockSumListCell());
    }

    public void refreshItemList() {
        Database.reloadItemsFromDatabase();
        ObservableList<Item> items = Database.getItemList();
        quickStockSummaryLIst.setItems(items);
    }

    public static DashStockSumController getInstance() {
        return instance;
    }

    public void loadLog(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventoryLogs();
    }

    public void loadArchive(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventoryArchive();
    }
}
