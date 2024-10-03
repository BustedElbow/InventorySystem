package com.company.inventory.controllers;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.models.Database;
import com.company.inventory.models.Sale;
import com.company.inventory.models.SaleDetails;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesController {


    @FXML private Label orderIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalPriceLabel;
    @FXML private ListView<String> saleListDetails;
    @FXML private ListView<Sale> saleList;

    public void initialize() {
        ObservableList<Sale> sales = Database.getSaleList();
        saleList.setItems(sales);

        saleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        saleList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displaySaleDetails(newSelection);
            }
        });

    }
    public void btnNewSale(ActionEvent event) {
        showModal();
    }

    public void showModal() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/newSale.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("New Sale");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displaySaleDetails(Sale sale) {
        orderIdLabel.setText("Order ID: " + sale.getSaleId());
        dateLabel.setText("Date: " + sale.getSaleDate());
        totalPriceLabel.setText("Total Price: " + sale.getTotalAmount());

        List<String> productDetails = SaleDetails.loadOrderDetails(sale.getSaleId());
        ObservableList<String> orderDetails = FXCollections.observableArrayList(productDetails);
        saleListDetails.setItems(orderDetails);
    }

}
