package com.example.greenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.snackbar.Snackbar;

public class PlantDetailActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private Plant plant;
    private int currentUserId = 1; // Default user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plant_detail);

        dbHelper = new DatabaseHelper(this);

        // Get plant ID from intent
        int plantId = getIntent().getIntExtra("PLANT_ID", -1);
        if (plantId == -1) {
            finish();
            return;
        }

        // Get plant from database
        plant = dbHelper.getPlantById(plantId);
        if (plant == null) {
            finish();
            return;
        }

        // Initialize views
        ImageView plantImage = findViewById(R.id.plant_detail_image);
        TextView plantName = findViewById(R.id.plant_detail_name);
        TextView plantPrice = findViewById(R.id.plant_detail_price);
        TextView plantDescription = findViewById(R.id.plant_detail_description);
        Button addToCartButton = findViewById(R.id.add_to_cart_button);
        ImageView favoriteButton = findViewById(R.id.favorite_button);
        ImageView closeButton = findViewById(R.id.close_button);
        closeButton.setOnClickListener(v -> finish());


        // Set plant data
        int imageResId = getResources().getIdentifier(
                plant.getImageName(), "drawable", getPackageName());
        if (imageResId != 0) {
            plantImage.setImageResource(imageResId);
        }

        plantName.setText(plant.getName());
        plantPrice.setText(String.format("$%.2f", plant.getPrice()));
        plantDescription.setText(plant.getDescription());

        // Set favorite button state
        boolean isFavorite = dbHelper.isFavorite(plant.getId(), currentUserId);
        favoriteButton.setImageResource(
                isFavorite ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);

        // Add to cart button
        addToCartButton.setOnClickListener(v -> {
            if (dbHelper.addToCart(plant.getId())) {
                Snackbar snackbar = Snackbar.make(
                        findViewById(android.R.id.content),
                        "Added " + plant.getName() + " to cart",
                        Snackbar.LENGTH_LONG);

                snackbar.setAction("VIEW CART", view -> {
                    startActivity(new Intent(PlantDetailActivity.this, CartActivity.class));
                });

                snackbar.setActionTextColor(getResources().getColor(R.color.green_leaf_dark));
                snackbar.show();
            } else {
                Toast.makeText(this, "Failed to add to cart", Toast.LENGTH_SHORT).show();
            }
        });

        // Favorite button
        favoriteButton.setOnClickListener(v -> {
            boolean newFavoriteState = !dbHelper.isFavorite(plant.getId(), currentUserId);
            if (newFavoriteState) {
                dbHelper.addFavorite(plant.getId(), currentUserId);
                Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.removeFavorite(plant.getId(), currentUserId);
                Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show();
            }
            favoriteButton.setImageResource(
                    newFavoriteState ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border);
        });
    }
}
