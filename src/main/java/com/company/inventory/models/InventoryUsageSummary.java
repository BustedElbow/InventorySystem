package com.company.inventory.models;

public class InventoryUsageSummary {
    private String itemName;
    private double totalQuantityUsed;
    private double currentStock;
    private String unitMeasure;


    public InventoryUsageSummary(String itemName, double totalQuantityUsed, double currentStock, String measure) {
        this.itemName = itemName;
        this.totalQuantityUsed = totalQuantityUsed;
        this.currentStock = currentStock;
        this.unitMeasure = measure;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getTotalQuantityUsed() {
        return totalQuantityUsed;
    }

    public void setTotalQuantityUsed(double totalQuantityUsed) {
        this.totalQuantityUsed = totalQuantityUsed;
    }

    public double getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(double currentStock) {
        this.currentStock = currentStock;
    }
}