package com.example.greenleaf;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewProducts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize product list
        productList = new ArrayList<>();
        productList.add(new Product("Aloe Vera", "Succulent plant", 10.0, R.drawable.aloe_vera));
        productList.add(new Product("Peace Lily", "Flowering plant", 15.0, R.drawable.peace_lily));
        // Add more products as needed

        adapter = new ProductAdapter(this, productList);
        recyclerView.setAdapter(adapter);
    }
}

