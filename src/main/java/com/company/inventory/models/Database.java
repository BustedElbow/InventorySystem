package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class Database {

    private static ObservableList<String> itemList = FXCollections.observableArrayList();

    static  {
        loadItemsFromDatabase();
    }

    private static void loadItemsFromDatabase() {
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

                String listItem = String.format("ID: %d | Name: %s | Unit: %s | Stock: %f | Reorder Level: %f", itemId, itemName, unitMeasure, itemStock, itemLevel);

                itemList.add(listItem);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public static ObservableList<String> getItemList() {
        return itemList;
    }

    public static void refreshItemList() {
        itemList.clear();
        loadItemsFromDatabase();
    }

    public static void addItemToList(String item) {
        itemList.add(item);
    }
}
