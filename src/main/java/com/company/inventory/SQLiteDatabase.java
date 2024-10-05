package com.company.inventory;

import java.sql.*;
import java.time.LocalDate;

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

                //Main tables

                String inventorySql = "CREATE TABLE IF NOT EXISTS items(item_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, item_name TEXT NOT NULL, unit_measure TEXT NOT NULL, stock_quantity REAL NOT NULL, reorder_level REAL NOT NULL)";
                stmt.executeUpdate(inventorySql);

                String productSql = "CREATE TABLE IF NOT EXISTS products(product_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, product_name TEXT NOT NULL, product_price DOUBLE NOT NULL)";
                stmt.executeUpdate(productSql);

                String productIngredientsSql = "CREATE TABLE IF NOT EXISTS product_ingredients(product_id INTEGER NOT NULL, item_id INTEGER NOT NULL, needed_quantity REAL NOT NULL, FOREIGN KEY(product_id) REFERENCES products(product_id), FOREIGN KEY(item_id) REFERENCES items(item_id), PRIMARY KEY(product_id, item_id))";
                stmt.executeUpdate(productIngredientsSql);

                String orderSql = "CREATE TABLE IF NOT EXISTS orders(order_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, order_date TEXT NOT NULL, total_amount REAL NOT NULL, is_included INTEGER DEFAULT 1)";
                stmt.executeUpdate(orderSql);

                String orderDetailSql = "CREATE TABLE IF NOT EXISTS order_details(order_id INTEGER NOT NULL, product_id INTEGER, name_at_trans TEXT NO NULL, price_at_trans REAL NOT NULL, quantity INTEGER NOT NULL, total_amount DOUBLE NOT NULL, FOREIGN KEY(order_id) REFERENCES orders(order_id), FOREIGN KEY(product_id) REFERENCES products(product_ID), PRIMARY KEY(order_id, product_id))";
                stmt.executeUpdate(orderDetailSql);

                String accessPinSql = "CREATE TABLE IF NOT EXISTS access_pin(pin_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, pin_code TEXT NOT NULL)";
                stmt.executeUpdate(accessPinSql);

                String inventoryLogSql = "CREATE TABLE IF NOT EXISTS inventory_log ("
                        + "log_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,"
                        + "item_id INTEGER NOT NULL,"
                        + "change_amount REAL NOT NULL,"
                        + "previous_quantity REAL NOT NULL,"
                        + "new_quantity REAL NOT NULL,"
                        + "change_type TEXT NOT NULL,"
                        + "reference_id INTEGER,"
                        + "timestamp TEXT NOT NULL,"
                        + "FOREIGN KEY (item_id) REFERENCES items(item_id)"
                        + ");";
                stmt.executeUpdate(inventoryLogSql);

                //Archive Tables

                String archiveProductSql = "CREATE TABLE IF NOT EXISTS archive_products(product_id INTEGER NOT NULL, product_name TEXT NOT NULL, product_price REAL NOT NULL, archive_date DATE NOT NULL)";
                stmt.executeUpdate(archiveProductSql);

                String archiveProdIngSql = "CREATE TABLE IF NOT EXISTS archive_prod_ingrd(product_id INTEGER NOT NULL, item_id INTEGER NOT NULL, item_name TEXT NOT NULL, needed_quantity REAL NOT NULL, unit_measure TEXT NOT NULL, archive_date TEXT NULL)";
                stmt.executeUpdate(archiveProdIngSql);

                String archiveInventorySql = "CREATE TABLE IF NOT EXISTS archive_items(item_id INTEGER NOT NULL, item_name TEXT NOT NULL, stock_quantity REAL NOT NULL, reorder_level REAL NOT NULL, unit_measure TEXT NOT NULL, archive_date TEXT NOT NULL)";
                stmt.executeUpdate(archiveInventorySql);

                System.out.println("Tables created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
