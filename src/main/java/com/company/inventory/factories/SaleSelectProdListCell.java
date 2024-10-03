package com.company.inventory.factory;

import com.company.inventory.controllers.NewSaleController;
import com.company.inventory.models.Product;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;


public class SaleSelectProdListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private Label productName = new Label();
    private Label price = new Label();
    private TextField quantityField = new TextField();
    private Button removeButton = new Button("-");
    private NewSaleController controller;
    public SaleSelectProdListCell(NewSaleController controller) {
        this.controller = controller;

        quantityField.setText("1");
        hbox.getChildren().addAll(removeButton,productName, quantityField, price);
        setGraphic(hbox);

        quantityField.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.isEmpty()) {
                try {
                    int quantity = Integer.parseInt(newVal);
                    controller.updateProductQuantity(getItem(), quantity);

                } catch (NumberFormatException e) {
                    System.out.println("Invalid value");
                }
            }
        });
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if (empty || product == null) {
            setGraphic(null);
        } else {
            price.setText(Double.toString(product.getProductPrice()));
            productName.setText(product.getProductName());
            removeButton.setOnAction(e -> {
                controller.removeProductFromSale(getItem());
            });

            setGraphic(hbox);
        }
    }
}
