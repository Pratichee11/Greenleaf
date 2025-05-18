package com.example.greenleaf;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.io.InputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductDetailActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView textViewName, textViewPrice, textViewDescription;
    private Button buttonAddToCart;
    private Button buttonBackToHome, buttonCheckout;

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
        buttonBackToHome = findViewById(R.id.buttonBackToHome);
        buttonCheckout = findViewById(R.id.buttonCheckout);


        product = (Product) getIntent().getSerializableExtra("product");

        if (product != null) {
            textViewName.setText(product.getName());
            textViewPrice.setText("$" + product.getPrice());
            textViewDescription.setText(product.getDescription());

            // Load image from URL or drawable
            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                new LoadImageTask(imageView).execute(product.getImageUrl());
            } else if (product.getImageResId() != 0) {
                imageView.setImageResource(product.getImageResId());
            } else {
                imageView.setImageResource(R.drawable.ic_cart); // Fallback image
            }

            buttonAddToCart.setOnClickListener(v -> {
                Cart.getInstance().addProduct(product);
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
            });

            buttonBackToHome.setOnClickListener(v -> {
                finish(); // simply goes back to HomeActivity
            });

            buttonCheckout.setOnClickListener(v -> {
                Intent intent = new Intent(ProductDetailActivity.this, CartActivity.class);
                startActivity(intent);
            });

        }
    }

    private static class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        private ImageView imageView;

        public LoadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                URL url = new URL(urls[0]);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true);
                conn.connect();
                InputStream in = conn.getInputStream();
                return BitmapFactory.decodeStream(in);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            } else {
                imageView.setImageResource(R.drawable.ic_cart);
            }
        }
    }
}
