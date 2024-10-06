package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class SaleDetails {

    private String productName;
    private int productQuantity;
    private double productPrice;

    public SaleDetails(String productName, int productQuantity, double productPrice) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
    }
    public String getProductName() {
        return productName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public static ObservableList<Sale> getSales() {
        ObservableList<Sale> sales = FXCollections.observableArrayList();
        String query = "SELECT order_id, order_date, total_amount, is_included FROM orders";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                // Get the order_date as a string from the ResultSet
                String orderDateTimeString = rs.getString("order_date");

                // Parse the date and time into a LocalDateTime
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateTimeString);

                // Create the Sale object with LocalDateTime instead of LocalDate
                Sale sale = new Sale(orderDateTime, rs.getDouble("total_amount"));
                sale.setSaleId(rs.getInt("order_id"));


                sales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sales;
    }


    public static List<SaleDetails> loadOrderDetails(int orderId) {
        List<SaleDetails> productDetails = new ArrayList<>();
        String query = "SELECT od.name_at_trans, od.quantity, od.price_at_trans " +
                "FROM order_details od " +
                "WHERE od.order_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Create SaleDetails object for each row
                    SaleDetails saleDetail = new SaleDetails(
                            rs.getString("name_at_trans"),
                            rs.getInt("quantity"),
                            rs.getDouble("price_at_trans")
                    );
                    productDetails.add(saleDetail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetails;
    }
}
