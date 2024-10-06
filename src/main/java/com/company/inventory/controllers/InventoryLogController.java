package com.company.inventory.controllers;

import com.company.inventory.factories.InventoryLogListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.InventoryLog;
import com.company.inventory.models.Item;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Collection;

public class InventoryLogController {
    private static InventoryLogController instance;
    @FXML
    public ListView<InventoryLog> logListView;

    public InventoryLogController() {
        instance = this;
    }
    public void initialize() {
        refreshLogItemList();
        logListView.setCellFactory(param -> new InventoryLogListCell());
    }

    public void refreshLogItemList() {
        Database.reloadInventoryLogsFromDatabase();
        ObservableList<InventoryLog> logs = Database.getInventoryLogs();

        FXCollections.reverse(logs);
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

    public void filterDateList(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filterLog.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Date Filter");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshLogListView() {
        logListView.refresh();
    }
}
