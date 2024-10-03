package com.company.inventory.controllers;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.io.InputStream;

public class LayoutController {

    private final String dashboardIconActive = "/icons/dashboard100-accent.png";
    private final String dashboardIconDefault = "/icons/dashboard100-white.png";

    private final String productIconActive = "/icons/goods100-accent.png";
    private final String productIconDefault = "/icons/goods100-white.png";

    private final String inventoryIconActive = "/icons/boxes100-accent.png";
    private final String inventoryIconDefault = "/icons/boxes100-white.png";

    private final String salesIconActive = "/icons/receipt100-accent.png";
    private final String salesIconDefault = "/icons/receipt100-white.png";

    @FXML private Button dashboard_btn;
    @FXML private Button products_btn;
    @FXML private Button inventory_btn;
    @FXML private Button sales_btn;
    @FXML private BorderPane borderPane;

    @FXML
    private void initialize() {
        loadInventory();
        loadDashboard();
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
        loadView("/fxml/inventory.fxml");
    }

    @FXML
    private void loadSales() {
        loadView("/fxml/sales.fxml");
    }

    @FXML
    private void loadView(String path) {
        try {
            Parent view = FXMLLoader.load(getClass().getResource(path));
            borderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button, String activeIcon) {
        resetButtonToDefault();

        button.getStyleClass().remove("default-button");
        button.getStyleClass().add("active-button");

        button.setGraphic(createIcon(activeIcon));
    }

    private void resetButtonToDefault() {
       dashboard_btn.getStyleClass().clear();
       dashboard_btn.getStyleClass().add("default-button");
       dashboard_btn.setGraphic(createIcon(dashboardIconDefault));

       products_btn.getStyleClass().clear();
       products_btn.getStyleClass().add("default-button");
       products_btn.setGraphic(createIcon(productIconDefault));

       inventory_btn.getStyleClass().clear();
       inventory_btn.getStyleClass().add("default-button");
       inventory_btn.setGraphic(createIcon(inventoryIconDefault));

       sales_btn.getStyleClass().clear();
       sales_btn.getStyleClass().add("default-button");
       sales_btn.setGraphic(createIcon(salesIconDefault));
    }

    private ImageView createIcon(String iconPath) {
        ImageView icon = null;
        InputStream iconStream = getClass().getResourceAsStream(iconPath);
        if (iconStream != null) {
            icon = new ImageView(new Image(iconStream));
            icon.setFitHeight(30);
            icon.setFitWidth(30);
        } else {
            System.err.println("Icon not found: " + iconPath);
        }
        return icon;
    }

    @FXML
    private void btnDashboard(ActionEvent event) {
        setActiveButton(dashboard_btn, dashboardIconActive);
        loadDashboard();
    }
    @FXML
    private void btnProducts(ActionEvent event) {
        setActiveButton(products_btn, productIconActive);
        loadProducts();
    }

    @FXML
    private void btnInventory(ActionEvent event) {
        setActiveButton(inventory_btn, inventoryIconActive);
        loadInventory();
    }

    @FXML
    private void btnSales(ActionEvent event) {
        setActiveButton(sales_btn, salesIconActive);
        loadSales();
    }

}
