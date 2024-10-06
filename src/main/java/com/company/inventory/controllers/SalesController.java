package com.company.inventory.controllers;

import com.company.inventory.factories.SaleDetailsListCell;
import com.company.inventory.factories.SaleListCell;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class SalesController {

    private static SalesController instance;
    @FXML private Label orderIdLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalPriceLabel;
    @FXML private ListView<SaleDetails> saleListDetails;
    @FXML public ListView<Sale> saleList;

    public SalesController() {
        instance = this;
    }

    public void initialize() {
        refreshSalesItemList();

        saleList.setCellFactory(param -> new SaleListCell());

        saleListDetails.setCellFactory(param -> new SaleDetailsListCell());

        saleList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);

        saleList.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                displaySaleDetails(newSelection);
            }
        });

    }

    public void refreshSalesItemList() {
        saleList.getItems().clear();
        Database.reloadSalesFromDatabase();
        ObservableList<Sale> sales = Database.getSaleList();
        FXCollections.reverse(sales);
        saleList.setItems(sales);
    }

    public static SalesController getInstance() {
        return instance;
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
        orderIdLabel.setText(Integer.toString(sale.getSaleId()));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
        String formattedDate = sale.getSaleDate().format(formatter);
        dateLabel.setText(formattedDate);
        totalPriceLabel.setText(Double.toString(sale.getTotalAmount()));

        List<SaleDetails> productDetails = SaleDetails.loadOrderDetails(sale.getSaleId());
        ObservableList<SaleDetails> orderDetails = FXCollections.observableArrayList(productDetails);
        saleListDetails.setItems(orderDetails);
    }

    public void filterDateList(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/filterSales.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Filter Date");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void refreshSaleList() {
        saleList.refresh();
    }
}
