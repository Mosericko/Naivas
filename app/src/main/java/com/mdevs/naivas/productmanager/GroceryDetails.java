package com.mdevs.naivas.productmanager;

public class GroceryDetails {
    String id;
    String image;
    String name;
    String price;
    String category;
    String quantity;

    public GroceryDetails(String id, String image, String name, String price, String category, String quantity) {
        this.id = id;
        this.image = image;
        this.name = name;
        this.price = price;
        this.category = category;
        this.quantity = quantity;
    }

    public String getId() {
        return id;
    }

    public String getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public String getQuantity() {
        return quantity;
    }
}
