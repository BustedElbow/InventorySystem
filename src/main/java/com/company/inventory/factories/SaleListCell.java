package com.company.inventory.factories;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.controllers.DashboardController;
import com.company.inventory.models.Sale;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SaleListCell extends ListCell<Sale> {
    private HBox hbox;
    private HBox actionBox = new HBox(5);
    private StackPane orderPane = new StackPane();
    private StackPane amountPane = new StackPane();
    private StackPane timePane = new StackPane();
    private StackPane actionPane = new StackPane();
    private Label orderIdLabel;
    private Label totalPriceLabel;
    private Label orderDateTimeLabel;
    private Button excludeButton;
    private Button includeButton;

    public SaleListCell() {
        orderIdLabel = new Label();
        totalPriceLabel = new Label();
        orderDateTimeLabel = new Label();
        excludeButton = new Button();
        includeButton = new Button();

        orderPane.getChildren().add(orderIdLabel);
        amountPane.getChildren().add(totalPriceLabel);
        timePane.getChildren().add(orderDateTimeLabel);
        actionPane.getChildren().add(actionBox);

        orderPane.setAlignment(Pos.CENTER_LEFT);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Image addImgSrc = new Image(getClass().getResource("/icons/check100-black.png").toString());
        ImageView addImg = new ImageView(addImgSrc);

        addImg.setFitHeight(25);
        addImg.setFitWidth(25);

        Image minusImgSrc = new Image(getClass().getResource("/icons/remove100-black.png").toString());
        ImageView minusImg = new ImageView(minusImgSrc);

        minusImg.setFitHeight(25);
        minusImg.setFitWidth(25);

        includeButton.setGraphic(addImg);
        includeButton.setStyle("-fx-background-color: #fefefe; -fx-background-radius: 4; -fx-cursor: hand; -fx-border-color: #929292; -fx-border-radius: 4;");

        excludeButton.setGraphic(minusImg);
        excludeButton.setStyle("-fx-background-color: #fefefe; -fx-background-radius: 4; -fx-cursor: hand; -fx-border-color: #929292; -fx-border-radius: 4;");

        actionBox.getChildren().addAll(excludeButton, includeButton);

        orderPane.setPrefWidth(120.25);
        amountPane.setPrefWidth(160.25);
        timePane.setPrefWidth(160.25);
        actionPane.setPrefWidth(149.25);


        hbox = new HBox(orderPane, amountPane, timePane, actionPane);
        hbox.setPrefWidth(585);
        setGraphic(hbox);


        // Exclude Button Action
        excludeButton.setOnAction(event -> {
            Sale sale = getItem();
            if (sale != null) {
                setIncludedToZero(sale.getSaleId());
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderDateTimeLabel.setStyle("-fx-text-fill: red;");
                orderIdLabel.setStyle("-fx-text-fill: red;");// Change label color to red
                DashboardController.getInstance().updateRevenueLabels();
            }
        });

        // Include Button Action
        includeButton.setOnAction(event -> {
            Sale sale = getItem();
            if (sale != null) {
                setIncludedToOne(sale.getSaleId());
                totalPriceLabel.setStyle("-fx-text-fill: black;");
                orderIdLabel.setStyle("-fx-text-fill: black;");
                orderDateTimeLabel.setStyle("-fx-text-fill: black;");// Reset label color
                DashboardController.getInstance().updateRevenueLabels();
            }
        });
    }

    protected void updateItem(Sale sale, boolean empty) {
        super.updateItem(sale, empty);
        if (empty || sale == null) {
            setGraphic(null);
        } else {
            orderIdLabel.setText("ID " + sale.getSaleId());
            totalPriceLabel.setText(String.format("Php %.2f", sale.getTotalAmount()));

            LocalDateTime saleDate = sale.getSaleDate();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm");
            String formattedDate = saleDate.format(formatter);
            orderDateTimeLabel.setText(formattedDate);

            boolean isIncluded = getIsIncludedFromDatabase(sale.getSaleId());

            if (isIncluded) {
                totalPriceLabel.setStyle("-fx-text-fill: black;");
                orderIdLabel.setStyle("-fx-text-fill: black;");
                orderDateTimeLabel.setStyle("-fx-text-fill: black;");
            } else {
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderDateTimeLabel.setStyle("-fx-text-fill: red;");
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

