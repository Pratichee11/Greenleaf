package com.example.greenleaf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {
    private Context context;
    private List<CartItem> cartItems;
    private OnCartItemClickListener listener;

    public interface OnCartItemClickListener {
        void onIncreaseQuantity(int cartId, int newQuantity);
        void onDecreaseQuantity(int cartId, int newQuantity);
        void onRemoveItem(int cartId);
    }

    public CartAdapter(Context context, List<CartItem> cartItems, OnCartItemClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);

        holder.plantName.setText(item.getPlantName());
        holder.plantPrice.setText(String.format("$%.2f", item.getPlantPrice()));
        holder.quantity.setText(String.valueOf(item.getQuantity()));
        holder.itemTotal.setText(String.format("$%.2f", item.getTotalPrice()));

        // Set image
        int imageResId = context.getResources().getIdentifier(
                item.getPlantImage(), "drawable", context.getPackageName());
        if (imageResId != 0) {
            holder.plantImage.setImageResource(imageResId);
        }

        // Button listeners
        holder.increaseButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() + 1;
            listener.onIncreaseQuantity(item.getCartId(), newQuantity);
        });

        holder.decreaseButton.setOnClickListener(v -> {
            int newQuantity = item.getQuantity() - 1;
            if (newQuantity > 0) {
                listener.onDecreaseQuantity(item.getCartId(), newQuantity);
            } else {
                listener.onRemoveItem(item.getCartId());
            }
        });

        holder.removeButton.setOnClickListener(v -> {
            listener.onRemoveItem(item.getCartId());
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView plantImage;
        TextView plantName, plantPrice, quantity, itemTotal;
        Button increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            plantImage = itemView.findViewById(R.id.cart_item_image);
            plantName = itemView.findViewById(R.id.cart_item_name);
            plantPrice = itemView.findViewById(R.id.cart_item_price);
            quantity = itemView.findViewById(R.id.cart_item_quantity);
            itemTotal = itemView.findViewById(R.id.cart_item_total);
            increaseButton = itemView.findViewById(R.id.increase_quantity);
            decreaseButton = itemView.findViewById(R.id.decrease_quantity);
            removeButton = itemView.findViewById(R.id.remove_item);
        }
    }
}