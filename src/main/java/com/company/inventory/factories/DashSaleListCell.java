package com.company.inventory.factories;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.controllers.DashboardController;
import com.company.inventory.models.Sale;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DashSaleListCell extends ListCell<Sale> {
    private HBox hbox;
    private Label orderIdLabel;
    private Label totalPriceLabel;
    private Label orderDateLabel;
    private Button viewItemsButton;
    private Button excludeButton;
    private Button includeButton;

    public DashSaleListCell() {
        orderIdLabel = new Label();
        totalPriceLabel = new Label();
        orderDateLabel = new Label();
        viewItemsButton = new Button("View Items");
        excludeButton = new Button("Exclude");
        includeButton = new Button("Include");

        hbox = new HBox(10, orderIdLabel, orderDateLabel, totalPriceLabel, excludeButton, includeButton);
        setGraphic(hbox);

        excludeButton.setOnAction(event -> {
            Sale sale = getItem();
            if (sale != null) {
                setIncludedToZero(sale.getSaleId());
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderDateLabel.setStyle("-fx-text-fill: red;");
                orderIdLabel.setStyle("-fx-text-fill: red;");
                DashboardController.getInstance().updateRevenueLabels();
            }
        });

        includeButton.setOnAction(event -> {
            Sale sale = getItem();
            if (sale != null) {
                setIncludedToOne(sale.getSaleId());
                totalPriceLabel.setStyle("-fx-text-fill: black;");
                orderIdLabel.setStyle("-fx-text-fill: black;");
                orderDateLabel.setStyle("-fx-text-fill: black;");
                DashboardController.getInstance().updateRevenueLabels();
            }
        });

    }

    @Override
    protected void updateItem(Sale sale, boolean empty) {
        super.updateItem(sale, empty);
        if (empty || sale == null) {
            setGraphic(null);
        } else {
            orderIdLabel.setText("Order ID: " + sale.getSaleId());
            totalPriceLabel.setText(String.format("Total: %.2f", sale.getTotalAmount()));

            boolean isIncluded = getIsIncludedFromDatabase(sale.getSaleId());

            if (isIncluded) {
                totalPriceLabel.setStyle("-fx-text-fill: black;");
                orderIdLabel.setStyle("-fx-text-fill: black;");
                orderDateLabel.setStyle("-fx-text-fill: black;");
            } else {
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderDateLabel.setStyle("-fx-text-fill: red;");
                orderIdLabel.setStyle("-fx-text-fill: red;");
            }

            setGraphic(hbox);
        }
    }
    private boolean getIsIncludedFromDatabase(int saleId) {
        String query = "SELECT is_included FROM orders WHERE order_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, saleId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("is_included") == 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void setIncludedToZero(int saleId) {
        String query = "UPDATE orders SET is_included = 0 WHERE order_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, saleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setIncludedToOne(int saleId) {
        String query = "UPDATE orders SET is_included = 1 WHERE order_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, saleId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}