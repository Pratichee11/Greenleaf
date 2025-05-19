package com.example.greenleaf;

public class Plant {
    private int id;
    private String name;
    private double price;
    private String description;
    private String imageName;
    private String category;

    public Plant(int id, String name, double price, String description, String imageName, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageName = imageName;
        this.category = category;
    }
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public String getDescription() { return description; }
    public String getImageName() { return imageName; }
    public String getCategory() { return category; }
}

