package com.mdevs.naivas.customer.classes;

public class CartDetails {
    String id;
    String name;
    String category;
    String price;
    String image;
    String selectedQuantity;
    String quantity;
    String totalPrice;


    public CartDetails() {
    }

    public CartDetails(String id, String name, String category, String price, String image, String selectedQuantity, String quantity,String totalPrice) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.image = image;
        this.selectedQuantity = selectedQuantity;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
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

    public String getSelectedQuantity() {
        return selectedQuantity;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setSelectedQuantity(String selectedQuantity) {
        this.selectedQuantity = selectedQuantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}
