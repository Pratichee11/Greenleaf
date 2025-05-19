package com.example.greenleaf;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);

        // Initialize toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout");
        }

        // Initialize views
        LinearLayout orderSummaryContainer = findViewById(R.id.order_summary_container);
        TextView totalPrice = findViewById(R.id.total_price);
        TextView deliveryDate = findViewById(R.id.delivery_date);
        Button confirmButton = findViewById(R.id.confirm_button);

        // Get cart items and populate summary
        List<CartItem> cartItems = dbHelper.getCartItems(); // Assume this method exists
        double total = 0;

        for (CartItem item : cartItems) {
            TextView itemView = new TextView(this);
            itemView.setText(String.format("%s - $%.2f", item.getPlantName(),item.getPlantPrice())); // ✅ use item not CartItem
            itemView.setTextSize(16);
            itemView.setTextColor(getResources().getColor(android.R.color.black));
            orderSummaryContainer.addView(itemView);
            total += item.getPlantPrice(); // ✅ use item not CartItem
        }


        // Set total price
        totalPrice.setText(String.format("$%.2f", total));

        // Set delivery date (3 days from now)
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        String date = sdf.format(new Date(System.currentTimeMillis() + 3L * 24 * 60 * 60 * 1000));
        deliveryDate.setText(date);

        // Confirm button action
        confirmButton.setOnClickListener(v -> {
            dbHelper.clearCart();
            Toast.makeText(this, "Order confirmed! Thank you for shopping with GreenLeaf", Toast.LENGTH_LONG).show();
            finish();
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
