package com.company.inventory.factories;

import com.company.inventory.SQLiteDatabase;
import com.company.inventory.controllers.*;
import com.company.inventory.models.InventoryLog;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import com.company.inventory.models.Item;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.company.inventory.SQLiteDatabase.connect;

public class InvArchListCell extends ListCell<Item> {
    private HBox hbox = new HBox();
    private HBox actionBox = new HBox(10);
    private Label id = new Label();
    private Label name = new Label();
    private Label unit = new Label();
    private Label level = new Label();
    private Label stock = new Label();
    private Button restoreButton = new Button("<");
    private StackPane unitPane = new StackPane();
    private StackPane stockPane = new StackPane();
    private StackPane namePane = new StackPane();
    private StackPane actionPane = new StackPane();
    private StackPane levelPane = new StackPane();

    //Logging
    private int logItemId;
    private String logItemName;
    private double logStockQuantity;


    public InvArchListCell() {

        restoreButton.setStyle("-fx-background-radius: 8; -fx-background-color: #ee8850; -fx-text-fill: #1e1e1e; -fx-padding: 8 12 8 12; -fx-font-family: 'Inter Semi Bold'; -fx-font-size: 16; -fx-cursor: hand;");

        namePane.setAlignment(Pos.CENTER_LEFT);
        actionPane.setAlignment(Pos.CENTER_RIGHT);
        actionBox.setAlignment(Pos.CENTER_RIGHT);

        id.setVisible(false);
        id.setManaged(false);

        actionBox.getChildren().addAll(restoreButton);

        namePane.getChildren().addAll(id, name);
        stockPane.getChildren().add(stock);
        levelPane.getChildren().add(level);
        unitPane.getChildren().add(unit);
        actionPane.getChildren().addAll(actionBox);

        levelPane.setPrefWidth(209);
        namePane.setPrefWidth(209);
        stockPane.setPrefWidth(209);
        unitPane.setPrefWidth(209);
        actionPane.setPrefWidth(209);

        hbox.setPrefWidth(1000);
        hbox.getChildren().addAll(namePane, stockPane, levelPane, unitPane, actionPane);

        setGraphic(hbox);
    }

    @Override
    protected void updateItem(Item item, boolean empty) {
        super.updateItem(item, empty);
        if (empty || item == null) {
            setGraphic(null);
        } else {
            id.setText(Integer.toString(item.getId()));
            name.setText(item.getName());
            unit.setText(String.valueOf(item.getUnitMeasure()));
            stock.setText(String.valueOf(item.getStock()));
            level.setText(String.valueOf(item.getReorderLevel()));
            restoreButton.setOnAction(e -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirm Restore");
                alert.setHeaderText("Are you sure you want to restore this item?");
                alert.setContentText("Item Name: " + name.getText());

                alert.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.OK) {
                        int archivedItemId = item.getId();
                        restoreItem(archivedItemId);
                        updateItem(item, false);
                        InventoryArchiveController.getInstance().refreshArcItemList();
                        InventoryController.getInstance().refreshItemList();
                        InventoryLog log = new InventoryLog(logItemId, logItemName, 0, logStockQuantity, logStockQuantity, "RESTORED", null);
                        log.logInventoryChange();
                    }
                });
            });
            setGraphic(hbox);
        }
    }

    private void restoreItem(int itemId) {
        String selectSql = "SELECT item_id, item_name, stock_quantity, reorder_level, unit_measure FROM archive_items WHERE item_id = ?";
        String insertSql = "INSERT INTO items(item_name, stock_quantity, reorder_level, unit_measure) VALUES(?, ?, ?, ?)";
        String deleteSql = "DELETE FROM archive_items WHERE item_id = ?";

        try (Connection conn = SQLiteDatabase.connect();
             PreparedStatement selectStmt = conn.prepareStatement(selectSql);
             PreparedStatement insertStmt = conn.prepareStatement(insertSql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteSql)) {

            conn.setAutoCommit(false);
            // Retrieve archived item
            selectStmt.setInt(1, itemId);
            ResultSet rs = selectStmt.executeQuery();

            if (rs.next()) {
                String itemName = rs.getString("item_name");
                double stockQuantity = rs.getDouble("stock_quantity");
                double reorderLevel = rs.getDouble("reorder_level");
                String unitMeasure = rs.getString("unit_measure");

                this.logItemId = itemId;
                this.logItemName = itemName;
                this.logStockQuantity = stockQuantity;

                // Insert the item into items table
                insertStmt.setString(1, itemName);
                insertStmt.setDouble(2, stockQuantity);
                insertStmt.setDouble(3, reorderLevel);
                insertStmt.setString(4, unitMeasure);
                insertStmt.executeUpdate();

                // Delete from archive_items
                deleteStmt.setInt(1, itemId);
                deleteStmt.executeUpdate();


                System.out.println("Item restored successfully.");
            } else {
                System.out.println("Item not found in archive.");
            }
            conn.commit();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
