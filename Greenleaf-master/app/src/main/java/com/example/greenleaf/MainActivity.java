package com.example.greenleaf;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.OptIn;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.badge.BadgeUtils;
import com.google.android.material.badge.ExperimentalBadgeUtils;
import com.google.android.material.snackbar.Snackbar;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements
        PlantAdapter.OnPlantClickListener {

    private RecyclerView plantsRecyclerView;
    private PlantAdapter plantAdapter;
    private DatabaseHelper dbHelper;
    private List<Plant> fullPlantList = new ArrayList<>(); // for search
    private List<Plant> plantList = new ArrayList<>(); // for display
    private BadgeDrawable badge;
    private SearchView searchView;

    @OptIn(markerClass = ExperimentalBadgeUtils.class)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DB
        dbHelper = new DatabaseHelper(this);

        // Set up toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // SearchView
        searchView = findViewById(R.id.searchView);

        // RecyclerView
        plantsRecyclerView = findViewById(R.id.plants_recycler_view);
        plantsRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Adapter setup
        plantAdapter = new PlantAdapter(this, plantList, this, 1); // userID = 1
        plantsRecyclerView.setAdapter(plantAdapter);

        // Badge setup
        ImageView cartIcon = findViewById(R.id.cart_icon);
        badge = BadgeDrawable.create(this);
        BadgeUtils.attachBadgeDrawable(badge, toolbar, R.id.cart_icon);

        // Load data
        loadPlants();
        updateCartBadge();

        // Cart icon click
        cartIcon.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CartActivity.class)));

        // Search filter
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterPlants(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterPlants(newText);
                return true;
            }
        });
    }

    private void loadPlants() {
        new Thread(() -> {
            List<Plant> loadedPlants = new ArrayList<>();
            Cursor cursor = dbHelper.getAllPlants();

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    loadedPlants.add(new Plant(
                            cursor.getInt(0),
                            cursor.getString(1),
                            cursor.getDouble(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5)
                    ));
                } while (cursor.moveToNext());
                cursor.close();
            }

            runOnUiThread(() -> {
                fullPlantList.clear();
                fullPlantList.addAll(loadedPlants);

                plantList.clear();
                plantList.addAll(loadedPlants);

                plantAdapter.notifyDataSetChanged();

                if (plantList.isEmpty()) {
                    Toast.makeText(this, "No plants found", Toast.LENGTH_SHORT).show();
                }
            });
        }).start();
    }

    private void filterPlants(String query) {
        List<Plant> filtered = new ArrayList<>();
        for (Plant plant : fullPlantList) {
            if (plant.getName().toLowerCase().contains(query.toLowerCase())) {
                filtered.add(plant);
            }
        }

        plantList.clear();
        plantList.addAll(filtered);
        plantAdapter.notifyDataSetChanged();
    }

    private void updateCartBadge() {
        if (badge != null) {
            int count = dbHelper.getCartItemCount();
            badge.setVisible(count > 0);
            badge.setNumber(count);
        }
    }

    @Override
    public void onAddToCartClick(Plant plant) {
        if (dbHelper.addToCart(plant.getId())) {
            updateCartBadge();
            Snackbar.make(findViewById(android.R.id.content),
                            "Added " + plant.getName() + " to cart",
                            Snackbar.LENGTH_LONG)
                    .setAction("VIEW", v -> startActivity(new Intent(this, CartActivity.class)))
                    .show();
        }
    }

    @Override
    public void onFavoriteClick(Plant plant, boolean isFavorite) {
        if (isFavorite) {
            dbHelper.addFavorite(plant.getId(), 1); // userID = 1
        } else {
            dbHelper.removeFavorite(plant.getId(), 1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }
}
