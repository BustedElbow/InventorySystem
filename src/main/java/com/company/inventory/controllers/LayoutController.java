package com.company.inventory.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class MainController {

    @FXML
    private Pane contentPane;

    @FXML
    public void initialize() {
        loadTest();
    }

    @FXML
    private void loadDashboard() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void loadTest() {
        loadView("/fxml/test.fxml");
    }

    private void loadView(String path) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(path));
            contentPane.getChildren().clear();
            contentPane.getChildren().add(view);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
