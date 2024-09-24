package com.company.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;


public class ItemModalController {

    @FXML private Button cancelBtn;
    @FXML private Button confirmBtn;

    @FXML
    private void btnCancel(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void btnConfirm(ActionEvent event) {
        System.out.println("Item Added... buhh");

        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
}
