package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    public static ObservableList<InventoryLog> filterLogsByDateRange(LocalDate fromDate, LocalDate toDate) {
        ObservableList<InventoryLog> filteredLogs = FXCollections.observableArrayList();
        String query = "SELECT item_id, item_name, change_amount, previous_quantity, new_quantity, change_type, reference_id, timestamp " +
                "FROM inventory_log WHERE DATE(timestamp) BETWEEN ? AND ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fromDate.toString());
            pstmt.setString(2, toDate.toString());
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

    public static ObservableList<Sale> filterSalesByDateRange(LocalDate fromDate, LocalDate toDate) {
        ObservableList<Sale> filteredSales = FXCollections.observableArrayList();
        String query = "SELECT * FROM orders WHERE DATE(order_date) BETWEEN ? AND ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fromDate.toString());
            pstmt.setString(2, toDate.toString());
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

    public static ObservableList<ProductSaleSummary> getSalesSummaryForDay(LocalDate date) {
        ObservableList<ProductSaleSummary> summaryList = FXCollections.observableArrayList();
        String query = "SELECT od.name_at_trans, od.price_at_trans, SUM(od.quantity) AS total_quantity, " +
                "SUM(od.quantity * od.price_at_trans) AS total_price " +
                "FROM orders o " +
                "JOIN order_details od ON o.order_id = od.order_id " +
                "WHERE DATE(o.order_date) = ? AND o.is_included = 1 " +
                "GROUP BY od.name_at_trans, od.price_at_trans";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, date.toString());

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name_at_trans");
                double price = rs.getDouble("price_at_trans");
                int totalQuantity = rs.getInt("total_quantity");
                double totalPrice = rs.getDouble("total_price");

                ProductSaleSummary summary = new ProductSaleSummary(productName, price, totalQuantity, totalPrice);
                summaryList.add(summary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return summaryList;
    }

    public static ObservableList<ProductSaleSummary> getSalesDetailsForDateRange(LocalDate fromDate, LocalDate toDate) {
        ObservableList<ProductSaleSummary> salesDetails = FXCollections.observableArrayList();
        String query = "SELECT od.name_at_trans, od.price_at_trans, SUM(od.quantity) AS total_quantity_sold, SUM(od.total_amount) AS total_price " +
                "FROM orders o JOIN order_details od ON o.order_id = od.order_id " +
                "WHERE DATE(o.order_date) BETWEEN ? AND ? AND o.is_included = 1 " +
                "GROUP BY od.name_at_trans, od.price_at_trans;";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, fromDate.toString());
            pstmt.setString(2, toDate.toString());
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String productName = rs.getString("name_at_trans");
                double priceAtTrans = rs.getDouble("price_at_trans");
                int totalQuantitySold = rs.getInt("total_quantity_sold");
                double totalPrice = rs.getDouble("total_price");

                ProductSaleSummary summary = new ProductSaleSummary(productName, priceAtTrans, totalQuantitySold, totalPrice);
                salesDetails.add(summary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return salesDetails;
    }

    public static ObservableList<InventoryUsageSummary> getInventoryUsageForDateRange(LocalDate fromDate, LocalDate toDate) {
        ObservableList<InventoryUsageSummary> inventorySummaries = FXCollections.observableArrayList();

        // Update the SQL query to use the correct format for timestamp filtering
        String query = "SELECT il.item_name, i.unit_measure, " +
                "SUM(CASE WHEN il.change_type IN ('SOLD') THEN il.change_amount ELSE 0 END) AS total_quantity_used, " +
                "MAX(il.new_quantity) AS current_stock " +
                "FROM inventory_log il " +
                "JOIN items i ON il.item_id = i.item_id " +
                "WHERE il.timestamp >= ? AND il.timestamp < ? " +
                "GROUP BY il.item_name, i.unit_measure";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, fromDate.atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
            pstmt.setString(2, toDate.plusDays(1).atStartOfDay().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String itemName = rs.getString("item_name");
                double totalQuantityUsed = rs.getDouble("total_quantity_used");
                double currentStock = rs.getDouble("current_stock");
                String unitMeasure = rs.getString("unit_measure");

                inventorySummaries.add(new InventoryUsageSummary(itemName, totalQuantityUsed, currentStock, unitMeasure));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Debugging output
        System.out.println("Fetched Inventory Summaries: " + inventorySummaries);

        return inventorySummaries;
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
