package com.example.greenleaf;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    private ListView listViewCart;
    private TextView textViewTotalPrice;
    private Button buttonCheckout;
    private List<Product> cartItems;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        listViewCart = findViewById(R.id.listViewCart);
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice);
        buttonCheckout = findViewById(R.id.buttonCheckout);


        cartItems = Cart.getInstance().getCartItems();
        List<String> itemNames = new ArrayList<>();
        double totalPrice = 0.0;
        for (Product p : cartItems) {
            itemNames.add(p.getName() + " - $" + p.getPrice());
            totalPrice += p.getPrice();
        }

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, itemNames);
        listViewCart.setAdapter(adapter);

        textViewTotalPrice.setText("Total: $" + String.format("%.2f", totalPrice));
    }
}
