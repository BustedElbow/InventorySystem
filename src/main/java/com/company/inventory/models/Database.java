package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Database {

    private static ObservableList<Item> itemList = FXCollections.observableArrayList();
    private static ObservableList<Product> productList = FXCollections.observableArrayList();
    private static ObservableList<Sale> saleList = FXCollections.observableArrayList();

    static  {
        loadItemsFromDatabase();
        loadProductsFromDatabase();
        loadOrdersFromDatabase();
    }

    public static void loadItemsFromDatabase() {
        String query = "SELECT * FROM items";

        try(Connection conn = SQLiteDatabase.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                String unitMeasure = rs.getString("unit_measure");
                double itemStock = rs.getDouble("stock_quantity");
                double itemLevel = rs.getDouble("reorder_level");

                Item item = new Item(itemName, unitMeasure, itemStock, itemLevel);
                item.setId(itemId);
                itemList.add(item);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadProductsFromDatabase() {
        String query = "SELECT * FROM products";

        try(Connection conn = SQLiteDatabase.connect();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double productPrice = rs.getDouble("product_price");

                Product product = new Product(productName, productPrice);
                product.setProductId(productId);
                productList.add(product);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadOrdersFromDatabase() {
        String query = "SELECT * FROM orders";

        try(Connection conn = SQLiteDatabase.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderDateString = rs.getString("order_date");
                LocalDate orderDate = LocalDate.parse(orderDateString);
                double totalAmount = rs.getDouble("total_amount");
                boolean isExcluded = rs.getInt("is_included") == 0;

                Sale sale = new Sale(orderDate, totalAmount);
                sale.setSaleId(orderId);
                saleList.add(sale);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<Item> getItemList() {
        return itemList;
    }
    public static ObservableList<Product> getProductList() {
        return productList;
    }
    public static ObservableList<Sale> getSaleList() {
        return saleList;
    }

    public static void addItemToList(Item item) {
        itemList.add(item);
    }
    public static void addProductToList(Product product) {
        productList.add(product);
    }
    public static void addSaleToList(Sale sale) {
        saleList.add(sale);
    }
    public static void reloadItemsFromDatabase() {
        itemList.clear();
        loadItemsFromDatabase();
    }
    public static void reloadProductsFromDatabase() {
        productList.clear();
        loadProductsFromDatabase();
    }
}
