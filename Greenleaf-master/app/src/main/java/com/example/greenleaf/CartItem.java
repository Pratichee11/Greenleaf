package com.example.greenleaf;

public class CartItem {
    private int cartId;
    private int plantId;
    private String plantName;
    private double plantPrice;
    private String plantImage;
    private int quantity;

    public CartItem(int cartId, int plantId, String plantName, double plantPrice, String plantImage, int quantity) {
        this.cartId = cartId;
        this.plantId = plantId;
        this.plantName = plantName;
        this.plantPrice = plantPrice;
        this.plantImage = plantImage;
        this.quantity = quantity;
    }

    // Getters
    public int getCartId() { return cartId; }
    public int getPlantId() { return plantId; }
    public String getPlantName() { return plantName; }
    public double getPlantPrice() { return plantPrice; }
    public String getPlantImage() { return plantImage; }
    public int getQuantity() { return quantity; }

    public double getTotalPrice() {
        return plantPrice * quantity;
    }
}