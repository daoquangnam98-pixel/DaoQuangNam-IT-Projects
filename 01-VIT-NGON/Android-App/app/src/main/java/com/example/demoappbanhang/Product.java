package com.example.demoappbanhang;

public class Product {

    private int id;
    private String name;
    private String description;
    private long price;
    private String category;
    private String image;
    private double rating;
    private int sold;

    public Product(int id, String name, String description, long price,
                   String category, String image, double rating, int sold) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.category = category;
        this.image = image;
        this.rating = rating;
        this.sold = sold;
    }

    public int getId() {
        return id;
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

    public String getCategory() {
        return category;
    }

    public String getImage() {
        return image;
    }

    public double getRating() {
        return rating;
    }

    public int getSold() {
        return sold;
    }
}