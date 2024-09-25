package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
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

    private void setId(int id) {
        this.id = id;
    }

    public void setUnitMeasure(String unit) {
        this.unitMeasure = unit;
    }

    public void setStock(double stock) {
        this.stock = stock;
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
                        System.out.println("Item inserted successfully");
                    }
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }
}
