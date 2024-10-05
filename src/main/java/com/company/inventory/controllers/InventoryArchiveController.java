package com.company.inventory.controllers;

import com.company.inventory.factories.InvArchListCell;
import com.company.inventory.factories.InventoryListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Item;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryArchiveController {

    private static InventoryArchiveController instance;
    @FXML
    private ListView<Item> itemListview;
    @FXML private Button addItemBtn;

    public InventoryArchiveController() {
        instance = this;
    };

    public void initialize(){
        refreshArcItemList();

        itemListview.getStylesheets().add(getClass().getResource("/styles/listview.css").toExternalForm());
        itemListview.setCellFactory(param -> new InvArchListCell());
    }

    @FXML
    private void btnAddItem(ActionEvent event) {
        showModal();
    }

    public void showModal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addItem.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Add Item");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshArcItemList() {
        Database.reloadArcItemsFromDatabase();
        ObservableList<Item> items = Database.getArcItemList();
        itemListview.setItems(items);
    }

    public static InventoryArchiveController getInstance() {
        return instance;
    }

    public void loadInventory(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventory();
    }

    public void loadLog(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventoryLogs();
    }
}
