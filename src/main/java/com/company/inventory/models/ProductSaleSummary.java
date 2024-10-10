package com.company.inventory.models;

public class ProductSaleSummary {
    private String productName;
    private double product_price;
    private int totalQuantity;
    private double totalPrice;

    public ProductSaleSummary(String productName, double product_price, int totalQuantity, double totalPrice) {
        this.productName = productName;
        this.product_price = product_price;
        this.totalQuantity = totalQuantity;
        this.totalPrice = totalPrice;
    }

    public String getProductName() {
        return productName;

    }

    public double getProductPrice() {
        return product_price;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }
}