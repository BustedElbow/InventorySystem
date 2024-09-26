package com.company.inventory.controllers;

import com.company.inventory.models.*;
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


public class InventoryController{


    @FXML private ListView<String> itemListview;
    @FXML private Button addItemBtn;

    private ObservableList<String> items;

    @FXML
    public void initialize(){
        items = Database.getItemList();
        itemListview.setItems(items);
    }

    @FXML
    private void btnAddItem(ActionEvent event) {
        showModal();
    }

    public void showModal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addItem.fxml"));
            Parent modalRoot = fxmlLoader.load();

            ItemModalController modalController = fxmlLoader.getController();

            modalController.setInventoryController(this);

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

    public void refreshList() {
        itemListview.refresh();
    }


}
