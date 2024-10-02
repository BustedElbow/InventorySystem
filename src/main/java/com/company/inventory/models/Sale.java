package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;

import java.sql.*;
import java.time.LocalDate;

public class Sale {
    private int saleId;
    private LocalDate saleDate;
    private double totalAmount;
    public Sale(LocalDate date, double amount) {
        this.saleDate = date;
        this.totalAmount = amount;
    }

    public void save() {
        String query = "INSERT INTO orders(order_date, total_amount) VALUES(?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            ps.setDate(1, Date.valueOf(saleDate));
            ps.setDouble(2, totalAmount);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    saleId = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addSaleDetail(Product product, int quantity) {
        double totalAmount = product.getProductPrice() * quantity;

        String query = "INSERT INTO order_details(order_id, product_id, quantity, total_amount) VALUES (?, ?, ?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, saleId);
            ps.setInt(2, product.getProductId());
            ps.setInt(3, quantity);
            ps.setDouble(4, totalAmount);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
