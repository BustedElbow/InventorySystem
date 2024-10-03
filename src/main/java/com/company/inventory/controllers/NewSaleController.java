package com.company.inventory.controllers;

import com.company.inventory.factories.SaleProdListCell;
import com.company.inventory.factories.SaleSelectProdListCell;
import com.company.inventory.models.Database;
import com.company.inventory.models.Product;
import com.company.inventory.models.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class NewSaleController {

    @FXML private Button btnConfirm;
    @FXML private ListView<Product> selectedProductList;
    @FXML private ListView<Product> availableProductList;
    @FXML private Label totalPrice = new Label();
    private ObservableList<Product> availableProducts;
    private ObservableList<Product> selectedProducts;
    private Map<Product, Integer> productQuantities = new HashMap<>();
    private double totalAmount = 0;

    public void initialize() {
        availableProducts = Database.getProductList();
        availableProductList.setItems(availableProducts);

        selectedProducts = FXCollections.observableArrayList();
        selectedProductList.setItems(selectedProducts);

        availableProductList.setCellFactory(param -> new SaleProdListCell(this));
        selectedProductList.setCellFactory(param -> new SaleSelectProdListCell(this));
    }

    public void addProductToSale(Product product) {
        if (!selectedProducts.contains(product)) {
            selectedProducts.add(product);
            productQuantities.put(product, 1);
            updateTotalAmount();
        }
    }

    public void removeProductFromSale(Product product) {
        if (selectedProducts.contains(product)) {
            selectedProducts.remove(product);
            productQuantities.remove(product);
            updateTotalAmount();
        }
    }

    private void updateTotalAmount() {
        totalAmount = 0;
        for (Product product : selectedProducts) {
            int quantity = productQuantities.get(product);
            totalAmount += product.getProductPrice() * quantity;
        }
        totalPrice.setText(Double.toString(totalAmount));
    }

    public void confirmNewSale(ActionEvent event) {
        Sale sale = new Sale(LocalDate.now(), totalAmount);
        sale.save();

        for (Product product : selectedProducts) {
            int quantity = productQuantities.get(product);
            sale.addSaleDetail(product, quantity);
        }

        Database.addSaleToList(sale);
        InventoryController.getInstance().refreshItemList();

        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();
    }

    public void updateProductQuantity(Product product, int quantity) {
        productQuantities.put(product, quantity);
        updateTotalAmount();
    }

    public void cancel(ActionEvent event) {

    }
}
