package com.example.greenleaf;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.Toast;


public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textViewName, textViewPrice, textViewDescription;
    private Button buttonAddToCart;
    private Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_detail);

        imageView = findViewById(R.id.imageViewDetail);
        textViewName = findViewById(R.id.textViewDetailName);
        textViewPrice = findViewById(R.id.textViewDetailPrice);
        textViewDescription = findViewById(R.id.textViewDetailDescription);
        buttonAddToCart = findViewById(R.id.buttonAddToCart);

        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            imageView.setImageResource(product.getImageResource());
            textViewName.setText(product.getName());
            textViewPrice.setText("$" + product.getPrice());
            textViewDescription.setText(product.getDescription());
        }

        buttonAddToCart.setOnClickListener(v -> {
            Cart.getInstance().addProduct(product);
            Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
        });
    }
}

