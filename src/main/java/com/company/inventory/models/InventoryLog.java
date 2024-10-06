package com.company.inventory.models;

import com.company.inventory.SQLiteDatabase;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;

public class InventoryLog {
    private int itemId;
    private String itemName;
    private double changeAmount;
    private double previousQuantity;
    private double newQuantity;
    private String changeType;
    private Integer referenceId;
    private String timestamp;

    public InventoryLog(int itemId, String itemName, double changeAmount, double previousQuantity, double newQuantity, String changeType, Integer referenceId, String timestamp) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.changeAmount = changeAmount;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.changeType = changeType;
        this.referenceId = referenceId;
        this.timestamp = timestamp;
    }

    public InventoryLog(int itemId, String itemName, double changeAmount, double previousQuantity, double newQuantity, String changeType, Integer referenceId) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.changeAmount = changeAmount;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.changeType = changeType;
        this.referenceId = referenceId;
    }

    // Getters
    public int getItemId() {
        return itemId;
    }
    public String getItemName() { return itemName; }
    public double getChangeAmount() { return changeAmount; }
    public double getPreviousQuantity() { return previousQuantity; }
    public double getNewQuantity() { return newQuantity; }
    public String getChangeType() { return changeType; }
    public Integer getReferenceId() { return referenceId; }
    public String getTimestamp() { return timestamp; }

    public void logInventoryChange() {
        String logQuery = "INSERT INTO inventory_log(item_id, item_name, change_amount, previous_quantity, new_quantity, change_type, reference_id, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = SQLiteDatabase.connect()) {

            try (PreparedStatement ps = conn.prepareStatement(logQuery)) {
                ps.setInt(1, this.itemId);
                ps.setString(2, this.itemName);
                ps.setDouble(3, this.changeAmount);
                ps.setDouble(4, this.previousQuantity);
                ps.setDouble(5, this.newQuantity);
                ps.setString(6, this.changeType);
                ps.setObject(7, this.referenceId); // Can be null if not applicable
                ps.setString(8, LocalDateTime.now().toString()); // Timestamp
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public String toString() {
        return String.format("ID, %d, Item: %s, Change: %.2f, Prev: %.2f, New: %.2f, Type: %s, Ref: %s, Time: %s",
                itemId, itemName, changeAmount, previousQuantity, newQuantity, changeType,
                referenceId != null ? referenceId.toString() : "N/A", timestamp);
    }
}
