package com.example.greenleaf;
import java.io.Serializable;
public class Product implements Serializable {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUrl;  // for remote URL, can be null if using drawable
    private int imageResId;   // for local drawable resource, 0 if none

    // Constructor for remote image URL
    public Product(int id, String name, String description, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.imageResId = 0;
    }

    // Constructor for local drawable resource
    public Product(int id, String name, String description, double price, int imageResId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageResId = imageResId;
        this.imageUrl = null;
    }
    public String getDescription() {
        return description;
    }
    // getters
    public String getImageUrl() { return imageUrl; }
    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    // ... other getters and setters
    public double getPrice() {
        return price;
    }
}
