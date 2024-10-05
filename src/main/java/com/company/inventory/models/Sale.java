package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class Sale {
    private int saleId;
    private LocalDateTime saleDateTime;
    private double totalAmount;
    public Sale(LocalDateTime dateTime, double amount) {
        this.saleDateTime = dateTime;
        this.totalAmount = amount;
    }

    public int getSaleId() {
        return this.saleId;
    }
    public LocalDateTime getSaleDate() {
        return this.saleDateTime;
    }

    public double getTotalAmount() {
        return this.totalAmount;
    }
    public void setSaleId(int id) {
        this.saleId = id;
    }

    public void save() {
        String query = "INSERT INTO orders(order_date, total_amount) VALUES(?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {


            ps.setString(1, LocalDateTime.now().toString());
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

        String nameAtTrans = product.getProductName();
        double priceAtTrans = product.getProductPrice();

        String query = "INSERT INTO order_details(order_id, product_id, name_at_trans, price_at_trans, quantity, total_amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = SQLiteDatabase.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, saleId);
            ps.setInt(2, product.getProductId());
            ps.setString(3, nameAtTrans);
            ps.setDouble(4, priceAtTrans);
            ps.setInt(5, quantity);
            ps.setDouble(6, totalAmount);
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

            double previousQuantity = getItemQuantity(itemId);

            String updateQuery = "UPDATE items SET stock_quantity = stock_quantity - ? WHERE item_id = ?";
            try (Connection conn = SQLiteDatabase.connect();
            PreparedStatement ps = conn.prepareStatement(updateQuery)) {

                ps.setDouble(1, neededQuantity);
                ps.setInt(2, itemId);
                ps.executeUpdate();

                double newQuantity = previousQuantity - neededQuantity;

                logInventoryChange(itemId, -neededQuantity, previousQuantity, newQuantity, "Sale", null);

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void logInventoryChange(int itemId, double changeAmount, double previousQuantity, double newQuantity, String changeType, Integer referenceId) {
        String logQuery = "INSERT INTO inventory_log(item_id, change_amount, previous_quantity, new_quantity, change_type, reference_id, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabase.connect()) {

            try (PreparedStatement ps = conn.prepareStatement(logQuery)) {
                ps.setInt(1, itemId);
                ps.setDouble(2, changeAmount);
                ps.setDouble(3, previousQuantity);
                ps.setDouble(4, newQuantity);
                ps.setString(5, changeType);
                ps.setObject(6, referenceId); // Can be null if not applicable
                ps.setString(7, LocalDateTime.now().toString()); // Timestamp
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private double getItemQuantity(int itemId) {
        String query = "SELECT stock_quantity FROM items WHERE item_id = ?";
        double quantity = 0;

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    quantity = rs.getDouble("stock_quantity");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return quantity;
    }

    public static double calculateDailyRevenue() {

        String query = "SELECT SUM(total_amount) AS total FROM orders WHERE DATE(order_date) = CURRENT_DATE AND is_included = 1";
        double total = 0;

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static double calculateMonthlyRevenue() {
        String query = "SELECT SUM(total_amount) AS total FROM orders WHERE strftime('%Y-%m', order_date) = strftime('%Y-%m', CURRENT_DATE) AND is_included = 1";
        double total = 0;

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                total = rs.getDouble("total");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static ObservableList<Sale> getRecentSalesToday() {
        ObservableList<Sale> recentSales = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders WHERE DATE(order_date) = CURRENT_DATE";

        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                LocalDateTime orderDateTime = LocalDateTime.parse(rs.getString("order_date"));
                double totalAmount = rs.getDouble("total_amount");

                Sale sale = new Sale(orderDateTime, totalAmount);
                sale.setSaleId(orderId);
                recentSales.add(sale);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return recentSales;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
        return this.saleId + this.saleDateTime.format(formatter) + this.totalAmount;
    }
}
