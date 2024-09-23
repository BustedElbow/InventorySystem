package com.company.inventory;

public class Product {
    private int productID;
    private String productName;

    private double price;

    public Product(int ID, String name, double price) {
        this.productID = ID;
        this.productName = name;
        this.price = price;
    }

}
