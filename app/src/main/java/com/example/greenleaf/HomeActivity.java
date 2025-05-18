package com.example.greenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.os.AsyncTask; // Import AsyncTask
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ImageView settingsButton;
    private ImageView cartButton;
    private EditText searchBar;
    private RecyclerView productList;
    private ProductAdapter productAdapter;
    private List<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize UI elements
        settingsButton = findViewById(R.id.settings_button);
        cartButton = findViewById(R.id.cart_button);
        searchBar = findViewById(R.id.search_bar);
        productList = findViewById(R.id.product_list);
        productList.setLayoutManager(new LinearLayoutManager(this));

        // Initialize product data in AsyncTask
        new LoadProductsTask().execute();

        // Set click listeners for buttons
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Settings Activity
                Intent intent = new Intent(HomeActivity.this, SettingsActivity.class); // Change to the actual activity
                startActivity(intent);
            }
        });

        cartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start Cart Activity
                Intent intent = new Intent(HomeActivity.this, CartActivity.class);  // Change to the actual activity
                startActivity(intent);
            }
        });

        searchBar.setOnEditorActionListener((v, actionId, event) -> {
            //When user clicks the search button on the keyboard.
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH) {
                performSearch(searchBar.getText().toString());
                return true;
            }
            return false;
        });



//        productAdapter.setOnItemClickListener(new ProductAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                // Start Detail Activity
//                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class); // change
//                intent.putExtra("product", (Serializable) products.get(position)); // Pass the whole product object
//                startActivity(intent);
//            }
//        });
    }

    private void performSearch(String query) {
        // Implement search functionality here
        // 1.  Filter the 'products' list based on the query.
        // 2.  Update the 'productAdapter' with the filtered list.
        // 3.  Notify the adapter that the data has changed.
        List<Product> filteredList = new ArrayList<>();
        for (Product product : products) {
            if (product.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(product);
            }
        }
        productAdapter.setProducts(filteredList); // Create and use this method in ProductAdapter
        productAdapter.notifyDataSetChanged();
        if (filteredList.isEmpty()){
            Toast.makeText(this, "No Plants Found", Toast.LENGTH_SHORT).show();
        }

    }

    // Method to get product list (replace with your actual data source)
    private List<Product> getProducts() {
        List<Product> productList = new ArrayList<>();
        productList.add(new Product(1, "Indoor Plant 1", "Beautiful indoor plant", 25.99, R.drawable.ic_cart));

        //productList.add(new Product(2, "Outdoor Plant 2", "Perfect for your garden.", 39.99, "R.drawable.ic_cart"));
//        productList.add(new Product(3, "Hanging Plant 3", "Lovely hanging plant.", 19.99, "https://www.pexels.com/photo/green-plants-2922353/"));
//        productList.add(new Product(4, "Succulent Plant 4", "Easy to care succulent.", 15.99, "https://via.placeholder.com/150"));
//        productList.add(new Product(5, "Indoor Plant 5", "Beautiful indoor plant for your home.", 25.99, "https://via.placeholder.com/150"));
//        productList.add(new Product(6, "Outdoor Plant 6", "Perfect for your garden.", 39.99, "https://via.placeholder.com/150"));
//        productList.add(new Product(7, "Hanging Plant 7", "Lovely hanging plant.", 19.99, "https://via.placeholder.com/150"));
//        productList.add(new Product(8, "Succulent Plant 8", "Easy to care succulent.", 15.99, "https://via.placeholder.com/150"));
        // Add more products as needed
        return productList;
    }

    // AsyncTask to load products in the background
    private class LoadProductsTask extends AsyncTask<Void, Void, List<Product>> {

        @Override
        protected void onPreExecute() {
            // Optional: Show a progress indicator (e.g., a ProgressBar)
            Log.d("HomeActivity", "LoadProductsTask started");
        }

        @Override
        protected List<Product> doInBackground(Void... voids) {
            // Simulate loading data (e.g., from a database or network)
            try {
                Thread.sleep(500); // Simulate a 500ms delay
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return getProducts(); // Load the products here
        }

        @Override
        protected void onPostExecute(List<Product> loadedProducts) {
            // Update the UI with the loaded products
            products = loadedProducts;
            productAdapter = new ProductAdapter(HomeActivity.this, products);
            productList.setAdapter(productAdapter);

            productAdapter.setOnItemClickListener(position -> {
                Intent intent = new Intent(HomeActivity.this, ProductDetailActivity.class);
                intent.putExtra("product", products.get(position));
                startActivity(intent);
            });

            Log.d("HomeActivity", "LoadProductsTask completed");
        }
    }
}
