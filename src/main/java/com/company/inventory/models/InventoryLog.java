package com.company.inventory.models;

public class InventoryLog {
    private String itemName;
    private double changeAmount;
    private double previousQuantity;
    private double newQuantity;
    private String changeType;
    private Integer referenceId;
    private String timestamp;

    public InventoryLog(String itemName, double changeAmount, double previousQuantity, double newQuantity, String changeType, Integer referenceId, String timestamp) {
        this.itemName = itemName;
        this.changeAmount = changeAmount;
        this.previousQuantity = previousQuantity;
        this.newQuantity = newQuantity;
        this.changeType = changeType;
        this.referenceId = referenceId;
        this.timestamp = timestamp;
    }

    // Getters
    public String getItemName() { return itemName; }
    public double getChangeAmount() { return changeAmount; }
    public double getPreviousQuantity() { return previousQuantity; }
    public double getNewQuantity() { return newQuantity; }
    public String getChangeType() { return changeType; }
    public Integer getReferenceId() { return referenceId; }
    public String getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("Item: %s, Change: %.2f, Prev: %.2f, New: %.2f, Type: %s, Ref: %s, Time: %s",
                itemName, changeAmount, previousQuantity, newQuantity, changeType,
                referenceId != null ? referenceId.toString() : "N/A", timestamp);
    }
}
