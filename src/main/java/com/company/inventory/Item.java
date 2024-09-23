package com.company.inventory;

public class Item {
    private int ID;
    private String name;
    private String unitMeasure;
    private double stockQuantity;
    private double reorderLevel;

    public Item(int ID, String name) {
        this.ID = ID;
        this.name = name;
        this.unitMeasure = "";
        this.stockQuantity = 0;
        this.reorderLevel = 0;
    }

    public void setUnitMeasure(String measure) {
        this.unitMeasure = measure;
    }

    public void setStockQuantity(double value) {
        this.stockQuantity += value;
    }

    public void setReorderLevel(double value) {
        this.reorderLevel = value;
    }

    public double getStockQuantity() {
        return this.stockQuantity;
    }

    public void decreaseStock(double value) {
        if(value <= this.stockQuantity) {
            this.stockQuantity -= value;
        }

    }
}
