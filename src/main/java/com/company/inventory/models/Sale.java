package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.controllers.InventoryController;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sale {
    private int saleId;
    private LocalDate saleDate;
    private double totalAmount;
    public Sale(LocalDate date, double amount) {
        this.saleDate = date;
        this.totalAmount = amount;
    }

    public int getSaleId() {
        return this.saleId;
    }

    public void setSaleId(int id) {
        this.saleId = id;
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

            deductProductIngredientsStock(product, quantity);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deductProductIngredientsStock(Product product, int quantitySold) {
        List<ProductIngredient> ingredients = product.getUsedItems();
        for (ProductIngredient ingredient : ingredients) {
            int itemId = ingredient.getItemId();
            double neededQuantity = ingredient.getNeededQuantity() * quantitySold;

            String updateQuery = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE item_id = ?";
            try (Connection conn = SQLiteDatabase.connect();
            PreparedStatement ps = conn.prepareStatement(updateQuery)) {

                ps.setDouble(1, neededQuantity);
                ps.setInt(2, itemId);
                ps.executeUpdate();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return this.saleId + this.saleDate.format(formatter) + this.totalAmount;
    }
}
