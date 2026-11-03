package com.example.demoappbanhang;

public class CartItem {

    private String name;
    private String description;
    private long price;
    private int quantity;
    private String image;

    public CartItem(
            String name,
            String description,
            long price,
            int quantity,
            String image
    ) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public long getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getImage() {
        return image;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getTotalPrice() {
        return price * quantity;
    }
}