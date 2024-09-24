package com.company.inventory.controllers;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class InventoryController{

    @FXML
    private Button addItemBtn;

    @FXML
    private void btnAddItem(ActionEvent event) throws IOException {
        showModal();
    }

    public void showModal() throws IOException {
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

}
