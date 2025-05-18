package com.example.greenleaf;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CheckoutActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        dbHelper = new DatabaseHelper(this);

        // Initialize views
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Checkout");
        }

        TextView orderSummary = findViewById(R.id.order_summary);
        TextView totalPrice = findViewById(R.id.total_price);
        TextView deliveryDate = findViewById(R.id.delivery_date);
        Button confirmButton = findViewById(R.id.confirm_button);

        // Set order details
        double total = dbHelper.getCartTotal();
        totalPrice.setText(String.format("$%.2f", total));

        // Calculate delivery date (3 days from now)
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, MMM d", Locale.getDefault());
        String date = sdf.format(new Date(System.currentTimeMillis() + 3 * 24 * 60 * 60 * 1000));
        deliveryDate.setText(date);

        // Confirm button
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