package com.company.inventory.factory;

import com.company.inventory.controllers.NewSaleController;
import com.company.inventory.models.Product;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.layout.HBox;

public class SaleProdListCell extends ListCell<Product> {
    private HBox hbox = new HBox();
    private Label productName = new Label();
    private Button addButton = new Button("+");
    private NewSaleController controller;
    public SaleProdListCell(NewSaleController controller) {
        this.controller = controller;

        hbox.getChildren().addAll(productName, addButton);

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Product product, boolean empty) {
        super.updateItem(product, empty);
        if (empty || product == null) {
            setGraphic(null);
        } else {
            productName.setText(product.getProductName());
            addButton.setOnAction(e -> {
                controller.addProductToSale(getItem());
            });

            setGraphic(hbox);
        }
    }

}
