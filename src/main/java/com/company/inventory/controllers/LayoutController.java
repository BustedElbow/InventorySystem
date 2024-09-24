package com.company.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

public class LayoutController {
    @FXML private Button dashboard_btn;
    @FXML private Button products_btn;
    @FXML private Button inventory_btn;
    @FXML private Button sales_btn;
    @FXML private BorderPane borderPane;


    @FXML
    public void initialize() {
        loadTest();
    }

    @FXML
    private void loadDashboard() {
        loadView("/fxml/dashboard.fxml");
    }

    @FXML
    private void loadProducts() {
        loadView("/fxml/product.fxml");
    }

    @FXML
    private void loadInventory() {
        setActiveButton(inventory_btn);
        loadView("/fxml/inventory.fxml");
    }

    @FXML
    private void loadSales() {
        setActiveButton(sales_btn);
        loadView("/fxml/sales.fxml");
    }

    @FXML
    private void loadTest() {
        loadView("/fxml/test.fxml");
    }

    private void loadView(String path) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(path));
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        resetButtonToDefault();

        button.getStyleClass().remove("default-button");
        button.getStyleClass().add("active-button");
    }

    private void resetButtonToDefault() {
       dashboard_btn.getStyleClass().clear();
       dashboard_btn.getStyleClass().add("default-button");

       products_btn.getStyleClass().clear();
       products_btn.getStyleClass().add("default-button");

       inventory_btn.getStyleClass().clear();
       inventory_btn.getStyleClass().add("default-button");

       sales_btn.getStyleClass().clear();
       sales_btn.getStyleClass().add("default-button");
    }


    @FXML
    private void btnDashboard(ActionEvent event) {
        setActiveButton(dashboard_btn);
        loadDashboard();
    }
    @FXML
    private void btnProducts(ActionEvent event) {
        setActiveButton(products_btn);
        loadProducts();
    }

    @FXML
    private void btnInventory(ActionEvent event) {
        loadInventory();
    }

    @FXML
    private void btnSales(ActionEvent event) {
        loadSales();
    }



}
