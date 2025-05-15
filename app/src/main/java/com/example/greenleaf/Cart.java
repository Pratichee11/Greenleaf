package com.example.greenleaf;
import java.util.ArrayList;
import java.util.List;

public class Cart {
    private static Cart instance;
    private List<Product> cartItems;

    private Cart() {
        cartItems = new ArrayList<>();
    }

    public static Cart getInstance() {
        if (instance == null) {
            instance = new Cart();
        }
        return instance;
    }

    public void addProduct(Product product) {
        cartItems.add(product);
    }

    public List<Product> getCartItems() {
        return cartItems;
    }

    public double getTotalPrice() {
        double total = 0;
        for (Product p : cartItems) {
            total += p.getPrice();
        }
        return total;
    }

    public void clearCart() {
        cartItems.clear();
    }
}

