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


    public static List<String> loadOrderDetails(int orderId) {
        List<String> productDetails = new ArrayList<>();
        String query = "SELECT od.name_at_trans, od.quantity, od.price_at_trans " +
                "FROM order_details od " +
                "WHERE od.order_id = ?";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, orderId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String detail = String.format("%s - Quantity: %d - Price: %.2f",
                            rs.getString("name_at_trans"),
                            rs.getInt("quantity"),
                            rs.getDouble("price_at_trans"));
                    productDetails.add(detail);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return productDetails;
    }
}
