package com.example.greenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity implements CartAdapter.OnCartItemClickListener {
    private RecyclerView cartRecyclerView;
    private CartAdapter cartAdapter;
    private DatabaseHelper dbHelper;
    private List<CartItem> cartItems;
    private TextView totalPrice;
    private Button checkoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Your Cart");
        }

        cartRecyclerView = findViewById(R.id.cart_recycler_view);
        totalPrice = findViewById(R.id.cart_total_price);
        checkoutButton = findViewById(R.id.checkout_button);

        // Set up RecyclerView
        cartRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load cart items
        loadCartItems();

        // Checkout button
        checkoutButton.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
            } else {
                startActivity(new Intent(CartActivity.this, CheckoutActivity.class));
            }
        });
    }

    private void loadCartItems() {
        cartItems = dbHelper.getCartItems();

        // Set up adapter
        cartAdapter = new CartAdapter(this, cartItems, this);
        cartRecyclerView.setAdapter(cartAdapter);

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        double total = dbHelper.getCartTotal();
        totalPrice.setText(String.format("Total: $%.2f", total));
    }

    @Override
    public void onIncreaseQuantity(int cartId, int newQuantity) {
        if (dbHelper.updateCartItemQuantity(cartId, newQuantity)) {
            loadCartItems();
        }
    }

    @Override
    public void onDecreaseQuantity(int cartId, int newQuantity) {
        if (dbHelper.updateCartItemQuantity(cartId, newQuantity)) {
            loadCartItems();
        }
    }

    @Override
    public void onRemoveItem(int cartId) {
        if (dbHelper.removeFromCart(cartId)) {
            loadCartItems();
            Toast.makeText(this, "Item removed from cart", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}