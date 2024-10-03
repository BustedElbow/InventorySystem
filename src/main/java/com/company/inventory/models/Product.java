package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Product {
    private int productId;
    private String productName;
    private double productPrice;
    public Product(String name, double price) {
        this.productName = name;
        this.productPrice = price;
    }

    public String getProductName() {
        return this.productName;
    }

    public int getProductId() {
        return this.productId;
    }

    public double getProductPrice() {
        return this.productPrice;
    }

    public void setProductId(int value) {
        this.productId = value;
    }

    public void setProductName(String name) {
        this.productName = name;
    }

    public void setProductPrice(Double price) {
        this.productPrice = price;
    }

    public void save() {
        String query = "INSERT INTO products(product_name, product_price) VALUES (?, ?)";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, productName);
            ps.setDouble(2, productPrice);

            int affectedRows = ps.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        this.productId = rs.getInt(1);
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void addProductIngredient(Item item, double neededQuantity) {
        String query = "INSERT INTO product_ingredients(product_id, item_id, needed_quantity) VALUES (?, ?, ?)";

        try (Connection conn = SQLiteDatabase.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, this.productId);
            ps.setInt(2, item.getId());
            ps.setDouble(3, neededQuantity);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public List<ProductIngredient> getUsedItems() {
        List<ProductIngredient> ingredients = new ArrayList<>();
        String query = "SELECT i.item_id, i.item_name, i.unit_measure, pi.needed_quantity FROM product_ingredients pi INNER JOIN items i on pi.item_id = i.item_id INNER JOIN products p on pi.product_id = p.product_id WHERE pi.product_id = ?";

        try (Connection conn = SQLiteDatabase.connect();
        PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setInt(1, this.productId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                ProductIngredient ingredient = new ProductIngredient(
                        rs.getInt("item_id"),
                        rs.getString("item_name"),
                        rs.getDouble("needed_quantity"),
                        rs.getString("unit_measure")
                );
                ingredients.add(ingredient);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return ingredients;
    }

    public void update() {
        String query = "UPDATE products SET product_name = ?, product_price = ? WHERE product_id = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, productName);
            ps.setDouble(2, productPrice);
            ps.setInt(3, productId);

            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public String toString() {
        return this.productId + this.productName + this.productPrice;
    }
}
