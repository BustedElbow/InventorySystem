package com.company.inventory;

import java.sql.*;

public class SQLiteDatabase {

    private static final String url = "jdbc:sqlite:system.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public static void initialize() {
        try(Connection conn = SQLiteDatabase.connect()) {
            if(conn != null) {
                Statement stmt = conn.createStatement();

                String inventorySql = "CREATE TABLE IF NOT EXISTS items(item_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, item_name TEXT NOT NULL, unit_measure TEXT NOT NULL, stock_quantity DOUBLE NOT NULL, reorder_level DOUBLE NOT NULL)";
                stmt.executeUpdate(inventorySql);

                String productSql = "CREATE TABLE IF NOT EXISTS products(product_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, product_name TEXT NOT NULL, product_price DOUBLE NOT NULL)";
                stmt.executeUpdate(productSql);

                String productIngredientsSql = "CREATE TABLE IF NOT EXISTS product_ingredients(product_id INTEGER NOT NULL, item_id INTEGER NOT NULL, needed_quantity DOUBLE NOT NULL, FOREIGN KEY(product_id) REFERENCES products(product_id), FOREIGN KEY(item_id) REFERENCES items(item_id), PRIMARY KEY(product_id, item_id))";
                stmt.executeUpdate(productIngredientsSql);

                String orderSql = "CREATE TABLE IF NOT EXISTS orders(order_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, order_date DATE NOT NULL, total_amount DOUBLE NOT NULL)";
                stmt.executeUpdate(orderSql);

                String orderDetailSql = "CREATE TABLE IF NOT EXISTS order_details(order_id INTEGER NOT NULL, product_id INTEGER NOT NULL, quantity INTEGER NOT NULL, total_amount DOUBLE NOT NULL, FOREIGN KEY(order_id) REFERENCES orders(order_id), FOREIGN KEY(product_id) REFERENCES products(product_ID), PRIMARY KEY(order_id, product_id))";
                stmt.executeUpdate(orderDetailSql);

                System.out.println("Tables created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
