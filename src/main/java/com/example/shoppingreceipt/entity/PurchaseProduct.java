package com.example.shoppingreceipt.entity;

import lombok.Data;

@Data
public class PurchaseProduct {
    private String productName;
    private int quantity;
    private String price;

    public PurchaseProduct(String productName, int quantity, String price) {
        this.productName = productName;
        this.quantity = quantity;
        this.price = price;
    }
}
