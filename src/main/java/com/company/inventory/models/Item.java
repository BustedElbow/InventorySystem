package com.company.inventory.models;

public class Item {
    private int id;
    private String name;
    private String unitMeasure;
    private double stock;
    private double reorderLevel;

    public Item(int id, String name, String unit, double stock, double reorderLevel) {
        this.id = id;
        this.name = name;
        this.unitMeasure = unit;
        this.stock = stock;
        this.reorderLevel = reorderLevel;
    }

    public void setUnitMeasure(String unit) {
        this.unitMeasure = unit;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public void setReorderLevel(double reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    public double getStock() {
        return stock;
    }

    public double getReorderLevel() {
        return reorderLevel;
    }
}
