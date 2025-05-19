package com.example.greenleaf;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class PlantAdapter extends RecyclerView.Adapter<PlantAdapter.PlantViewHolder> {
    private Context context;
    private List<Plant> plantList;
    private DatabaseHelper dbHelper;
    private int currentUserId;
    private OnPlantClickListener listener;

    public interface OnPlantClickListener {
        void onAddToCartClick(Plant plant);
        void onFavoriteClick(Plant plant, boolean isFavorite);
    }

    public PlantAdapter(Context context, List<Plant> plantList, OnPlantClickListener listener, int currentUserId) {
        this.context = context;
        this.plantList = plantList;
        this.listener = listener;
        this.dbHelper = new DatabaseHelper(context);
        this.currentUserId = currentUserId;
    }

    @NonNull
    @Override
    public PlantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.plant_card_item, parent, false);
        return new PlantViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlantViewHolder holder, int position) {
        Plant plant = plantList.get(position);

        // Set plant data
        holder.plantName.setText(plant.getName());
        holder.plantPrice.setText(String.format("$%.2f", plant.getPrice()));

        // Load image - ensure these drawables exist!
        int resId = context.getResources().getIdentifier(
                plant.getImageName(),
                "drawable",
                context.getPackageName());

        if (resId != 0) {
            holder.plantImage.setImageResource(resId);
        } else {

            Log.e("PlantAdapter", "Missing image: " + plant.getImageName());
        }

        // Set click listeners
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, PlantDetailActivity.class);
            intent.putExtra("PLANT_ID", plant.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return plantList.size();
    }

    public void updatePlants(List<Plant> newPlants) {
        this.plantList.clear();
        this.plantList.addAll(newPlants);

    }

    public static class PlantViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage, favoriteButton;
        TextView plantName, plantPrice;
        Button addToCartButton;

        public PlantViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.plant_image);
            plantName = itemView.findViewById(R.id.plant_name);
            plantPrice = itemView.findViewById(R.id.plant_price);
            addToCartButton = itemView.findViewById(R.id.add_to_cart_button);
            favoriteButton = itemView.findViewById(R.id.favorite_button);
        }
    }
    public void updateList(List<Plant> newList) {
        plantList = newList;
        notifyDataSetChanged();
    }

}