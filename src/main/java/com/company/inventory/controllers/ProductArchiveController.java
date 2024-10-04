package com.company.inventory.controllers;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

public class ProductArchiveController {

    public ListView archProdListView;
    public ListView archProdDetailsList;
    public Label productDetailsName;
    public Label productDetailsPrice1;
    public Label productDetailsPrice;
    public Button productsBtn;

    public void showProducts(ActionEvent actionEvent) {
        LayoutController.getInstance().loadProducts();
    }
}
