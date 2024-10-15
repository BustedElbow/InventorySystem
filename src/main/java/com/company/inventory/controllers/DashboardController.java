package com.company.inventory.controllers;


import com.company.inventory.factories.DashSaleListCell;
import com.company.inventory.factories.LowStockListCell;
import com.company.inventory.models.Item;
import com.company.inventory.models.Sale;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

public class DashboardController {

    private static DashboardController instance;
    @FXML private Button quickSummary;
    @FXML private Label goodStockLabel;
    @FXML private ImageView alertImage;
    @FXML private ListView<Sale> listDashSales;
    @FXML private ListView<Item> listLowStock;
    @FXML private Label revenueThisDay;
    @FXML private Label revenueThisMonth;

    public DashboardController() {
        instance = this;
    }

    public static DashboardController getInstance() {
        return instance;
    }

    public void btnNewSale(ActionEvent actionEvent) {
        showModal();
    }
    public void initialize() {
        loadLowStockItems();
        updateRevenueLabels();
        loadRecentSalesToday();

        listDashSales.setCellFactory(param -> new DashSaleListCell());
        listLowStock.setCellFactory(param -> new LowStockListCell());
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

    public void showStockSummary() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/quickSummary.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Stock Summary");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadLowStockItems() {
        ObservableList<Item> lowStockItems = Item.getLowStockItems();
        listLowStock.setItems(lowStockItems);

        if (lowStockItems.isEmpty()) {
            alertImage.setVisible(false);
            goodStockLabel.setVisible(true);
        } else {
            alertImage.setVisible(true);
            goodStockLabel.setVisible(false);
        }
    }

    public void updateRevenueLabels() {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(Locale.getDefault()); // Use the system default locale, or specify one

        revenueThisDay.setText(currencyFormat.format(Sale.calculateDailyRevenue()));
        revenueThisMonth.setText(currencyFormat.format(Sale.calculateMonthlyRevenue()));
    }

    public void loadRecentSalesToday() {
        listDashSales.getItems().clear();
        ObservableList<Sale> recentSales = Sale.getRecentSalesToday();
        FXCollections.reverse(recentSales);
        listDashSales.setItems(recentSales);
    }

    public void quickViewStocks(ActionEvent actionEvent) {
        showStockSummary();
    }


    public void generateReport(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/reportFilter.fxml"));
            Parent modalRoot = fxmlLoader.load();

            Stage modalStage = new Stage();

            modalStage.setResizable(false);
            modalStage.setTitle("Generate Report");
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setScene(new Scene(modalRoot));
            modalStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
