package com.company.inventory.controllers;

import com.company.inventory.factory.ProductListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.io.IOException;

public class ProductController {

    @FXML private ListView<Product> productListView;

    @FXML
    public void initialize() {
        ObservableList<Product> products = Database.getProductList();
        productListView.setItems(products);

        productListView.setCellFactory(param -> new ProductListCell());

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
