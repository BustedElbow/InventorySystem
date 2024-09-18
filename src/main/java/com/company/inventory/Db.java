package com.company.inventory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Logger;

public class Db {
    private Connection connection;
    private Logger logger = Logger.getLogger(this.getClass().getName());
    public void getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:sqlite:test.db");
                logger.info("Connected to Database");
                createTable();
            }
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void createTable() {
        getConnection();
        String query = "create table if not exists test (id integer not null primary key autoincrement, title text not null, content text not null)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.executeUpdate();
            logger.info("Table Created");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }

    private void closeConnection() throws SQLException {
        if (connection != null || !connection.isClosed()) {
            connection.close();
        }
    }

    public void insertNote(String title, String content) {
        getConnection();
        String query = "insert into test (title, content) values (?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, content);
            statement.executeUpdate();
            logger.info("Note inserted");
        } catch (SQLException e) {
            logger.info(e.toString());
        }
    }


}
