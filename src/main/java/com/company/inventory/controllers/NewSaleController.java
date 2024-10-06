package com.company.inventory.controllers;

import com.company.inventory.factories.SaleProdListCell;
import com.company.inventory.factories.SaleSelectProdListCell;
import com.company.inventory.models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewSaleController {

    @FXML private Label dateLabel;
    @FXML private Button btnCancel;
    @FXML private Button btnConfirm;
    @FXML private ListView<Product> selectedProductList;
    @FXML private ListView<Product> availableProductList;
    @FXML private Label totalPrice = new Label();
    private ObservableList<Product> availableProducts;
    private ObservableList<Product> selectedProducts;
    private Map<Product, Integer> productQuantities = new HashMap<>();
    private double totalAmount = 0;

    public void initialize() {
        dateLabel.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("MMM dd, yyyy")));
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

        if (selectedProducts.isEmpty()) {
            showError("Please add products to the sale before confirming.");
            return;
        }

        if (!isStockSufficient()) {
            return;
        }

        Sale sale = new Sale(LocalDateTime.now(), totalAmount);
        sale.save();

        for (Product product : selectedProducts) {
            int quantity = productQuantities.get(product);
            sale.addSaleDetail(product, quantity);
        }

        Database.addSaleToList(sale);
        InventoryController.getInstance().refreshItemList();
        DashboardController.getInstance().loadLowStockItems();
        DashboardController.getInstance().updateRevenueLabels();
        DashboardController.getInstance().loadRecentSalesToday();
        InventoryLogController.getInstance().refreshLogItemList();
        SalesController.getInstance().refreshSalesItemList();

        Stage stage = (Stage) btnConfirm.getScene().getWindow();
        stage.close();
    }

    public void updateProductQuantity(Product product, int quantity) {
        productQuantities.put(product, quantity);
        updateTotalAmount();
    }

    public void cancel(ActionEvent event) {
        Stage stage = (Stage) btnCancel.getScene().getWindow();
        stage.close();
    }

    private boolean isStockSufficient() {
        StringBuilder errorMessage = new StringBuilder();
        boolean sufficient = true;

        for (Product product : selectedProducts) {
            int productQuantity = productQuantities.get(product);

            // Get the list of ingredients (items) used by the product
            List<ProductIngredient> ingredients = product.getUsedItems();

            for (ProductIngredient ingredient : ingredients) {
                Item item = ingredient.getItem(); // Get the item from the ingredient
                double neededQuantity = ingredient.getNeededQuantity() * productQuantity; // Total needed quantity for this sale

                if (item.getStock() < neededQuantity) {
                    sufficient = false; // Mark as insufficient
                    errorMessage.append("Insufficient stock for product: ")
                            .append(product.getProductName())
                            .append(" - Item: ")
                            .append(item.getName())
                            .append(" (needed: ")
                            .append(neededQuantity)
                            .append(", available: ")
                            .append(item.getStock())
                            .append(")\n");
                }
            }
        }

        if (!sufficient) {
            showError(errorMessage.toString()); // Show detailed error message
        }

        return sufficient;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Stock Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
