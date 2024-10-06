package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;

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
        double previousStock = this.stock; // Store previous stock for logging
        double previousReorderLevel = this.reorderLevel; // Store previous reorder level for logging


        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, this.name);
            ps.setString(2, this.unitMeasure);
            ps.setDouble(3, this.stock);
            ps.setDouble(4, this.reorderLevel);
            ps.setInt(5, this.id);

            ps.executeUpdate();

            InventoryLog log = new InventoryLog(this.id, this.name, 0, previousStock, this.stock, "UPDATE", null);
            log.logInventoryChange();
        } catch (SQLException e) {
            System.out.println("Error updating item: " + e.getMessage());
        }
    }

    public static Item getItemById(int itemId) {
        String query = "SELECT item_id, item_name, unit_measure, stock_quantity, reorder_level FROM items WHERE item_id = ?";
        Item item = null;

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, itemId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                item = new Item(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getString("unit_measure"),
                        rs.getDouble("stock_quantity"),
                        rs.getDouble("reorder_level")
                );
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return item;  // Return the Item object or null if not found
    }

    public void restock(double restock) {
        if (this.id <= 0) {
            throw new IllegalArgumentException("Cannot restock item without a valid ID.");
        }

        double previousStock = this.stock; // Store previous stock for logging
        this.stock += restock;

        String query = "UPDATE items SET stock_quantity = ? WHERE item_ID = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setDouble(1, this.stock);
            ps.setInt(2, this.id);

            ps.executeUpdate();


            InventoryLog log = new InventoryLog(this.id, this.name, +restock, previousStock, this.stock, "RESTOCK", null);
            log.logInventoryChange();
        } catch (SQLException e) {
            System.out.println("Error restocking item: " + e.getMessage());
        }
    }

    public void delete() {
        if (this.id <= 0) {
            throw new IllegalArgumentException("Cannot delete item without a valid ID.");
        }

        archiveItem();

        String query = "DELETE FROM items WHERE item_ID = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, this.id);

            ps.executeUpdate();

            InventoryLog log = new InventoryLog(this.id, this.name, -this.stock, this.stock, 0, "DELETED", null);
            log.logInventoryChange();
        } catch (SQLException e) {
            System.out.println("Error deleting item: " + e.getMessage());
        }
    }

    private void archiveItem() {
        String query = "INSERT INTO archive_items(item_id, item_name, stock_quantity, reorder_level, unit_measure, archive_date) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, this.id);
            ps.setString(2, this.name);
            ps.setDouble(3, this.stock);
            ps.setDouble(4, this.reorderLevel);
            ps.setString(5, this.unitMeasure);
            ps.setString(6, LocalDateTime.now().toString());

            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error archiving item: " + e.getMessage());
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
