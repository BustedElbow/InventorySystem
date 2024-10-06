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
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {

    private static DashboardController instance;
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
        revenueThisDay.setText(String.format("%.2f", Sale.calculateDailyRevenue()));
        revenueThisMonth.setText(String.format("%.2f", Sale.calculateMonthlyRevenue()));
    }

    public void loadRecentSalesToday() {
        listDashSales.getItems().clear();
        ObservableList<Sale> recentSales = Sale.getRecentSalesToday();
        FXCollections.reverse(recentSales);
        listDashSales.setItems(recentSales);
    }
}
