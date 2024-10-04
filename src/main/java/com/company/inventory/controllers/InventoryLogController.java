package com.company.inventory.controllers;

import javafx.event.ActionEvent;

public class InventoryLogController {
    public void btnAddItem(ActionEvent actionEvent) {
    }

    public void loadInventory(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventory();
    }

    public void loadArchive(ActionEvent actionEvent) {
        LayoutController.getInstance().loadInventoryArchive();
    }
}
