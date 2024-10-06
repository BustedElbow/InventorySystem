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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DashSaleListCell extends ListCell<Sale> {
    private HBox hbox;
    private HBox actionBox = new HBox();
    private StackPane orderPane = new StackPane();
    private StackPane amountPane = new StackPane();
    private StackPane timePane = new StackPane();
    private StackPane actionPane = new StackPane();
    private Label orderIdLabel;
    private Label totalPriceLabel;
    private Label orderTimeLabel;
    private Button viewItemsButton;
    private Button excludeButton;
    private Button includeButton;

    public DashSaleListCell() {
        orderIdLabel = new Label();
        totalPriceLabel = new Label();
        orderTimeLabel = new Label();
        viewItemsButton = new Button("View Items");
        excludeButton = new Button();
        includeButton = new Button();

        orderPane.getChildren().add(orderIdLabel);
        amountPane.getChildren().add(totalPriceLabel);
        timePane.getChildren().add(orderTimeLabel);
        actionPane.getChildren().add(actionBox);

        orderPane.setAlignment(Pos.CENTER_LEFT);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        Image addImgSrc = new Image(getClass().getResource("/icons/check100-accent.png").toString());
        ImageView addImg = new ImageView(addImgSrc);

        addImg.setFitHeight(30);
        addImg.setFitWidth(30);

        Image minusImgSrc = new Image(getClass().getResource("/icons/remove100-accent.png").toString());
        ImageView minusImg = new ImageView(minusImgSrc);

        minusImg.setFitHeight(30);
        minusImg.setFitWidth(30);

        includeButton.setGraphic(addImg);
        includeButton.setStyle("-fx-background-color: #fefefe; -fx-background-radius: 4; -fx-cursor: hand;");

        excludeButton.setGraphic(minusImg);
        excludeButton.setStyle("-fx-background-color: #fefefe; -fx-background-radius: 4; -fx-cursor: hand;");

        actionBox.getChildren().addAll(excludeButton, includeButton);

        orderPane.setPrefWidth(257);
        amountPane.setPrefWidth(257);
        timePane.setPrefWidth(257);
        actionPane.setPrefWidth(250);


        hbox = new HBox(orderPane, amountPane, timePane, actionPane);
        hbox.setPrefWidth(1018);
        setGraphic(hbox);

        excludeButton.setOnAction(event -> {
            Sale sale = getItem();
            if (sale != null) {
                setIncludedToZero(sale.getSaleId());
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderTimeLabel.setStyle("-fx-text-fill: red;");
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
                orderTimeLabel.setStyle("-fx-text-fill: black;");
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
            orderIdLabel.setText("ID " + sale.getSaleId());
            totalPriceLabel.setText(String.format("Php %.2f", sale.getTotalAmount()));

            String formattedDateTime = formatOrderTime(sale.getSaleDate());
            orderTimeLabel.setText(formattedDateTime);

            boolean isIncluded = getIsIncludedFromDatabase(sale.getSaleId());

            if (isIncluded) {
                totalPriceLabel.setStyle("-fx-text-fill: black;");
                orderIdLabel.setStyle("-fx-text-fill: black;");
                orderTimeLabel.setStyle("-fx-text-fill: black;");
            } else {
                totalPriceLabel.setStyle("-fx-text-fill: red;");
                orderTimeLabel.setStyle("-fx-text-fill: red;");
                orderIdLabel.setStyle("-fx-text-fill: red;");
            }

            setGraphic(hbox);
        }
    }

    private String formatOrderTime(LocalDateTime orderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        return orderDate.format(formatter);
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