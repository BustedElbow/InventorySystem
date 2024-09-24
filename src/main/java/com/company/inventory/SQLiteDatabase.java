package com.company.inventory;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDatabase {

    public void initialize() {
        String url = "jdbc:sqlite:system.db";

        try(Connection conn = DriverManager.getConnection(url)) {
            if(conn != null) {
                Statement stmt = conn.createStatement();

                String sql = "CREATE TABLE IF NOT EXISTS items(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, item_name TEXT NOT NULL, unit_measure TEXT NOT NULL, stock_quantity DOUBLE NOT NULL, reorder_level DOUBLE NOT NULL)";
                stmt.executeUpdate(sql);
                System.out.println("Table Created");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
