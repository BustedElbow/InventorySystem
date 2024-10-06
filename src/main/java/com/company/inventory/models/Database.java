package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDateTime;

public class Database {

    private static ObservableList<Item> itemList = FXCollections.observableArrayList();
    private static ObservableList<Item> arcItemList = FXCollections.observableArrayList();
    private static ObservableList<Product> productList = FXCollections.observableArrayList();
    private static ObservableList<Product> archivedProductList = FXCollections.observableArrayList();
    private static ObservableList<Sale> saleList = FXCollections.observableArrayList();
    private static ObservableList<InventoryLog> logs = FXCollections.observableArrayList();


    static  {
        loadItemsFromDatabase();
        loadArcItemsFromDatabase();
        loadProductsFromDatabase();
        loadArchivedProductsFromDatabase();
        loadOrdersFromDatabase();
        loadInventoryLogs();
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
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateString);
                double totalAmount = rs.getDouble("total_amount");

                Sale sale = new Sale(orderDateTime, totalAmount);
                sale.setSaleId(orderId);
                saleList.add(sale);
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadArchivedProductsFromDatabase() {
        String query = "SELECT * FROM archive_products"; // Adjust this query as necessary

        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int productId = rs.getInt("product_id");
                String productName = rs.getString("product_name");
                double productPrice = rs.getDouble("product_price");

                Product product = new Product(productName, productPrice);
                product.setProductId(productId);
                archivedProductList.add(product); // Add to the archived products list
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadArcItemsFromDatabase() {
        String query = "SELECT * FROM archive_items";

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
                arcItemList.add(item);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static void loadInventoryLogs() {
        String query = "SELECT item_id, item_name, change_amount, previous_quantity, new_quantity, change_type, reference_id, timestamp " +
                "FROM inventory_log";

        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int itemId= rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double changeAmount = rs.getDouble("change_amount");
                double previousQuantity = rs.getDouble("previous_quantity");
                double newQuantity = rs.getDouble("new_quantity");
                String changeType = rs.getString("change_type");
                Integer referenceId = rs.getInt("reference_id");
                String timestamp = rs.getString("timestamp");

                InventoryLog log = new InventoryLog(itemId,itemName, changeAmount, previousQuantity, newQuantity, changeType, referenceId, timestamp);
                logs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static ObservableList<String> getDistinctYearsLogs() {
        ObservableList<String> years = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%Y', timestamp) AS year FROM inventory_log ORDER BY year";

        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                years.add(rs.getString("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }

    public static ObservableList<String> getDistinctMonthsLogs(String year) {
        ObservableList<String> months = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%m', timestamp) AS month FROM inventory_log WHERE strftime('%Y', timestamp) = ? ORDER BY month";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                months.add(rs.getString("month"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return months;
    }

    public static ObservableList<String> getDistinctDaysLogs(String year, String month) {
        ObservableList<String> days = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%d', timestamp) AS day FROM inventory_log WHERE strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ? ORDER BY day";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                days.add(rs.getString("day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static ObservableList<InventoryLog> filterLogsByDate(String year, String month, String day) {
        ObservableList<InventoryLog> filteredLogs = FXCollections.observableArrayList();
        String query = "SELECT item_id, item_name, change_amount, previous_quantity, new_quantity, change_type, reference_id, timestamp " +
                "FROM inventory_log WHERE strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ? AND strftime('%d', timestamp) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            pstmt.setString(3, day);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double changeAmount = rs.getDouble("change_amount");
                double previousQuantity = rs.getDouble("previous_quantity");
                double newQuantity = rs.getDouble("new_quantity");
                String changeType = rs.getString("change_type");
                Integer referenceId = rs.getInt("reference_id");
                String timestamp = rs.getString("timestamp");

                InventoryLog log = new InventoryLog(itemId, itemName, changeAmount, previousQuantity, newQuantity, changeType, referenceId, timestamp);
                filteredLogs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredLogs;
    }

    public static ObservableList<InventoryLog> filterLogsByYear(String year) {
        ObservableList<InventoryLog> filteredLogs = FXCollections.observableArrayList();
        String query = "SELECT * FROM inventory_log WHERE strftime('%Y', timestamp) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double changeAmount = rs.getDouble("change_amount");
                double previousQuantity = rs.getDouble("previous_quantity");
                double newQuantity = rs.getDouble("new_quantity");
                String changeType = rs.getString("change_type");
                Integer referenceId = rs.getInt("reference_id");
                String timestamp = rs.getString("timestamp");

                InventoryLog log = new InventoryLog(itemId, itemName, changeAmount, previousQuantity, newQuantity, changeType, referenceId, timestamp);
                filteredLogs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredLogs;
    }

    public static ObservableList<InventoryLog> filterLogsByYearAndMonth(String year, String month) {
        ObservableList<InventoryLog> filteredLogs = FXCollections.observableArrayList();
        String query = "SELECT * FROM inventory_log WHERE strftime('%Y', timestamp) = ? AND strftime('%m', timestamp) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int itemId = rs.getInt("item_id");
                String itemName = rs.getString("item_name");
                double changeAmount = rs.getDouble("change_amount");
                double previousQuantity = rs.getDouble("previous_quantity");
                double newQuantity = rs.getDouble("new_quantity");
                String changeType = rs.getString("change_type");
                Integer referenceId = rs.getInt("reference_id");
                String timestamp = rs.getString("timestamp");

                InventoryLog log = new InventoryLog(itemId, itemName, changeAmount, previousQuantity, newQuantity, changeType, referenceId, timestamp);
                filteredLogs.add(log);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredLogs;
    }


    public static ObservableList<String> getDistinctYearsSales() {
        ObservableList<String> years = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%Y', order_date) AS year FROM orders ORDER BY year";

        try (Connection conn = SQLiteDatabase.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                years.add(rs.getString("year"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return years;
    }

    public static ObservableList<String> getDistinctMonthsSales(String year) {
        ObservableList<String> months = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%m', order_date) AS month FROM orders WHERE strftime('%Y', order_date) = ? ORDER BY month";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                months.add(rs.getString("month"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return months;
    }

    public static ObservableList<String> getDistinctDaysSales(String year, String month) {
        ObservableList<String> days = FXCollections.observableArrayList();
        String query = "SELECT DISTINCT strftime('%d', order_date) AS day FROM orders WHERE strftime('%Y', order_date) = ? AND strftime('%m', order_date) = ? ORDER BY day";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                days.add(rs.getString("day"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return days;
    }

    public static ObservableList<Sale> filterSalesByDate(String year, String month, String day) {
        ObservableList<Sale> filteredSales = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders WHERE strftime('%Y', order_date) = ? AND strftime('%m', order_date) = ? AND strftime('%d', order_date) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            pstmt.setString(3, day);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderDateString = rs.getString("order_date");
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateString);
                double totalAmount = rs.getDouble("total_amount");

                Sale sale = new Sale(orderDateTime, totalAmount);
                sale.setSaleId(orderId);
                filteredSales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredSales;
    }

    public static ObservableList<Sale> filterSalesByYear(String year) {
        ObservableList<Sale> filteredSales = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders WHERE strftime('%Y', order_date) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderDateString = rs.getString("order_date");
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateString);
                double totalAmount = rs.getDouble("total_amount");

                Sale sale = new Sale(orderDateTime, totalAmount);
                sale.setSaleId(orderId);
                filteredSales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredSales;
    }

    public static ObservableList<Sale> filterSalesByYearAndMonth(String year, String month) {
        ObservableList<Sale> filteredSales = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders WHERE strftime('%Y', order_date) = ? AND strftime('%m', order_date) = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, year);
            pstmt.setString(2, month);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int orderId = rs.getInt("order_id");
                String orderDateString = rs.getString("order_date");
                LocalDateTime orderDateTime = LocalDateTime.parse(orderDateString);
                double totalAmount = rs.getDouble("total_amount");

                Sale sale = new Sale(orderDateTime, totalAmount);
                sale.setSaleId(orderId);
                filteredSales.add(sale);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return filteredSales;
    }

    public static ObservableList<Item> getItemList() {
        return itemList;
    }
    public static ObservableList<Item> getArcItemList() { return  arcItemList;}
    public static ObservableList<Product> getProductList() {
        return productList;
    }
    public static ObservableList<Product> getArchivedProductList() {
        return archivedProductList;
    }
    public static ObservableList<Sale> getSaleList() {
        return saleList;
    }
    public static ObservableList<InventoryLog> getInventoryLogs() {
        return logs;
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

    public static void reloadSalesFromDatabase() {
        saleList.clear();
        loadOrdersFromDatabase();
    }
    public static void reloadArchProdFromDatabase() {
        archivedProductList.clear();
        loadArchivedProductsFromDatabase();
    }

    public static void reloadArcItemsFromDatabase() {
        arcItemList.clear();
        loadArcItemsFromDatabase();
    }

    public static void reloadInventoryLogsFromDatabase() {
        logs.clear();
        loadInventoryLogs();
    }
}
