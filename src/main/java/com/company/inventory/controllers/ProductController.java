package com.company.inventory.controllers;

import com.company.inventory.factories.ProdDetailsListCell;
import com.company.inventory.factories.ProductListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import com.company.inventory.models.ProductIngredient;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.List;


public class ProductController {

    @FXML private ListView<ProductIngredient> productDetailsList;
    @FXML private Label productDetailsName;
    @FXML private Label productDetailsPrice;
    @FXML private ListView<Product> productListView;

    @FXML
    public void initialize() {
        ObservableList<Product> products = Database.getProductList();
        productListView.setItems(products);

        productListView.setCellFactory(param -> new ProductListCell());

        productListView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displayProductDetails(newSelection);
            }
        });

        productDetailsList.setCellFactory(param -> new ProdDetailsListCell());
    }

    private void displayProductDetails(Product product) {
        productDetailsList.getItems().clear();

        productDetailsName.setText(product.getProductName());
        productDetailsPrice.setText(Double.toString(product.getProductPrice()));

        List<ProductIngredient> ingredients = product.getUsedItems();
        ObservableList<ProductIngredient> usedItemObs = FXCollections.observableArrayList(ingredients);

        productDetailsList.setItems(usedItemObs);
    }

    public void showModal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/addProduct.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.setResizable(false);
            modalStage.setTitle("Add Product");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
