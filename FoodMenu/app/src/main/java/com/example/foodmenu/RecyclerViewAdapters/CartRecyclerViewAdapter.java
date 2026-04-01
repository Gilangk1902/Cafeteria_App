package com.example.foodmenu.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.CartHandler;
import com.example.foodmenu.DataBaseHandler.DrinkHandler;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.DataBaseHandler.OnDataBindCompleteListener;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.R;

import java.util.ArrayList;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<CartItem> cart_items;
    private final ArrayList<Integer> prices;
    private final Context context;
    private final OnDataBindCompleteListener callback;
    private ArrayList<Integer> itemTotals = new ArrayList<>();
    public CartRecyclerViewAdapter(ArrayList<CartItem> cartItems,
                                   ArrayList<Integer> prices,
                                   Context context,
                                   OnDataBindCompleteListener callback) {
        this.cart_items = cartItems;
        this.prices = prices;
        this.context = context;
        this.callback = callback;
        itemTotals = new ArrayList<>();
        for(int i = 0; i < cart_items.size(); i++){
            itemTotals.add(0);
        }
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {
        CartItem item = cart_items.get(position);
        holder.item_count_TextView.setText(String.valueOf(item.getQuantity()));
        holder.price_TextView.setText(String.valueOf(prices.get(position)));
        holder.title_TextView.setText(item.getId());
        String cartItemId = cart_items.get(position).getId();
        // Listeners are already set inside ViewHolder
        if (cartItemId.contains(Food.CODE)) {
            Bind_Food(holder, position);
        } else if (cartItemId.contains(Drink.CODE)) {
            Bind_Drink(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return cart_items.size();
    }

    private void Bind_Food(ViewHolder holder, int position) {
        FoodHandler foodHandler = new FoodHandler();

        foodHandler.setIntoTextView(
                cart_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        foodHandler.setIntoTextView_Price(
                cart_items.get(position).getId(),
                cart_items.get(position).getQuantity(),
                holder.price_TextView,
                price -> {
                    itemTotals.set(position, price); // ✅ store per item total
                    callback.onDataBindComplete();
                }
        );

        foodHandler.setIntoImageView(
                cart_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    public ArrayList<CartItem> getCartItems() {
        return cart_items;
    }
    public ArrayList<Integer> getItemTotals(){
        return itemTotals;
    }
    private void Bind_Drink(ViewHolder holder, int position) {
        DrinkHandler drinkHandler = new DrinkHandler();

        drinkHandler.setIntoTextView(
                cart_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        drinkHandler.setIntoTextView_Price(
                cart_items.get(position).getId(),
                cart_items.get(position).getQuantity(),
                holder.price_TextView,
                price -> {
                    itemTotals.set(position, price); // ✅ store per item total
                    callback.onDataBindComplete();
                }
        );

        drinkHandler.setIntoImageView(
                cart_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        TextView price_TextView, title_TextView, item_count_TextView;
        ImageView image_ImageView;
        Button min_Button, plus_Button;
        CartHandler cartHandler = new CartHandler();

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            price_TextView = itemView.findViewById(R.id.item_price_TextView);
            title_TextView = itemView.findViewById(R.id.item_title_TextView);
            item_count_TextView = itemView.findViewById(R.id.item_count_TextView);
            image_ImageView = itemView.findViewById(R.id.item_image_ImageView);
            min_Button = itemView.findViewById(R.id.minus_Button);
            plus_Button = itemView.findViewById(R.id.plus_Button);

            plus_Button.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                cartHandler.ModifyQuantity(
                        CartHandler.PLUS,
                        Session.getUser().getId(),
                        cart_items.get(position).getId()
                );

                // Update the quantity in adapter immediately
                cart_items.get(position).setQuantity(cart_items.get(position).getQuantity() + 1);
                notifyItemChanged(position);

                callback.onDataBindComplete(); // notify fragment to recalc total
            });

            min_Button.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position == RecyclerView.NO_POSITION) return;

                int currentQty = cart_items.get(position).getQuantity();
                if (currentQty > 1) {
                    cartHandler.ModifyQuantity(
                            CartHandler.MINUS,
                            Session.getUser().getId(),
                            cart_items.get(position).getId()
                    );

                    // Update the quantity in adapter immediately
                    cart_items.get(position).setQuantity(currentQty - 1);
                    notifyItemChanged(position);

                    callback.onDataBindComplete(); // notify fragment to recalc total
                }
            });
        }
    }
}