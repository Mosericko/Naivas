package com.mdevs.naivas.customer.classes;

public class OrderItems {
    String name, quantity, totalPrice, price,stock;

    public OrderItems(String name, String quantity, String totalPrice, String price) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.price = price;
    }

    public OrderItems(String name, String quantity, String totalPrice, String price, String stock) {
        this.name = name;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }
}
