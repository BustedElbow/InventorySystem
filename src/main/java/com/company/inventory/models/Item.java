package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
    private int id;
    private String name;
    private String unitMeasure;
    private double stock;
    private double reorderLevel;

    public Item(String name, String unit, double stock, double reorderLevel) {
        this.name = name;
        this.unitMeasure = unit;
        this.stock = stock;
        this.reorderLevel = reorderLevel;
    }

    public Item(int id, String name, String unit, double stock, double reorderLevel) {
        this.id = id;
        this.name = name;
        this.unitMeasure = unit;
        this.stock = stock;
        this.reorderLevel = reorderLevel;
    }

    public void setName(String name) {
        this.name = name;
    }
    public void setStock(Double stock) {
        this.stock = stock;
    }
    public void setId(int id) {
        this.id = id;
    }

    public void setUnitMeasure(String unit) {
        this.unitMeasure = unit;
    }

    public void setReorderLevel(double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return this.name;
    }

    public String getUnitMeasure() {
        return this.unitMeasure;
    }

    public double getStock() {
        return this.stock;
    }

    public double getReorderLevel() {
        return this.reorderLevel;
    }

    public void addStock(double stock) {
        this.stock += stock;
    }

    public void save() {
        String query = "INSERT INTO items(item_name, unit_measure, stock_quantity, reorder_level) VALUES(?, ?, ?, ?)";

        try(Connection conn = SQLiteDatabase.connect();
        PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, this.name);
            ps.setString(2, this.unitMeasure);
            ps.setDouble(3, this.stock);
            ps.setDouble(4, this.reorderLevel);

            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try(ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        int id = rs.getInt(1);
                        setId(id);
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update() {
        if (this.id <= 0) {
            throw new IllegalArgumentException("Cannot update item without a valid ID.");
        }

        String query = "UPDATE items SET item_name = ?, unit_measure = ?, stock_quantity = ?, reorder_level = ? WHERE item_ID = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, this.name);
            ps.setString(2, this.unitMeasure);
            ps.setDouble(3, this.stock);
            ps.setDouble(4, this.reorderLevel);
            ps.setInt(5, this.id);  // Set the ID to update the correct record

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e.getMessage());
        }
    }

    public void delete() {
        if (this.id <= 0) {
            throw new IllegalArgumentException("Cannot delete item without a valid ID.");
        }

        String query = "DELETE FROM items WHERE item_ID = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, this.id);

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    public static ObservableList<Item> getLowStockItems() {
        ObservableList<Item> lowStockItems = FXCollections.observableArrayList();
        String query = "SELECT * FROM items WHERE stock_quantity <= reorder_level";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                int id = rs.getInt("item_ID");
                String name = rs.getString("item_name");
                String unitMeasure = rs.getString("unit_measure");
                double stock = rs.getDouble("stock_quantity");
                double reorderLevel = rs.getDouble("reorder_level");

                Item item = new Item(id, name, unitMeasure, stock, reorderLevel);
                lowStockItems.add(item);
            }
        } catch (SQLException e) {
            System.out.println("Error fetching low stock items: " + e.getMessage());
        }

        return lowStockItems;
    }
    public String toString() {
        return this.name + this.unitMeasure + this.stock + this.reorderLevel;
    }
}
