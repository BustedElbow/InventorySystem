package com.company.inventory.controllers;

import com.company.inventory.factories.ArchProdListCell;
import com.company.inventory.factories.ProdDetailsListCell;
import com.company.inventory.factories.ProductListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import com.company.inventory.models.ProductIngredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.util.List;

public class ProductArchiveController {
    private static ProductArchiveController instance;
    @FXML private ListView<Product> archProdListView; // ListView for archived products
    @FXML private ListView<ProductIngredient> archProdDetailsList; // ListView for product ingredients
    @FXML private Label productDetailsName; // Label for product name
    @FXML private Label productDetailsPrice; // Label for product price
    @FXML private Button productsBtn; // Button to go back to product list

    public ProductArchiveController() {
        instance = this;
    }
    @FXML
    public void initialize() {
        refreshArcProdList();

        archProdListView.setCellFactory(param -> new ArchProdListCell());


        archProdListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayArchivedProductDetails(newSelection);
            }
        });

        archProdDetailsList.setCellFactory(param -> new ProdDetailsListCell());
    }

    private void displayArchivedProductDetails(Product product) {
        archProdDetailsList.getItems().clear();

        productDetailsName.setText(product.getProductName());
        productDetailsPrice.setText(Double.toString(product.getProductPrice()));

        List<ProductIngredient> ingredients = product.getArcUsedItems();
        ObservableList<ProductIngredient> usedItemObs = FXCollections.observableArrayList(ingredients);

        archProdDetailsList.setItems(usedItemObs);
    }

    public void refreshArcProdList() {
        Database.reloadArchProdFromDatabase();
        ObservableList<Product> products = Database.getArchivedProductList();
        archProdListView.setItems(products);
    }

    public static ProductArchiveController getInstance() {
        return instance;
    }

    public void showProducts(ActionEvent actionEvent) {
        LayoutController.getInstance().loadProducts();
    }
}
