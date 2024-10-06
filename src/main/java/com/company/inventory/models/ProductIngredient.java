package com.company.inventory.models;

import java.util.Objects;

public class ProductIngredient {

    private int itemId;
    private String itemName;
    private double neededQuantity;
    private String unitMeasure;
    private Item item;

    public ProductIngredient(int itemId, String itemName, double neededQuantity, String unitMeasure) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.neededQuantity = neededQuantity;
        this.unitMeasure = unitMeasure;
        this.item = Item.getItemById(itemId);
    }

    public Item getItem() {
        return this.item;
    }
    public int getItemId() { return itemId; }
    public String getItemName() { return itemName; }
    public double getNeededQuantity() { return neededQuantity; }
    public String getUnitMeasure() { return unitMeasure; }

}

