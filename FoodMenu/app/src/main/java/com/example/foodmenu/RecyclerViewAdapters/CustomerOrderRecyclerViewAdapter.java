package com.example.foodmenu.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.DataBaseHandler.DrinkHandler;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.R;

import java.util.ArrayList;

public class CustomerOrderRecyclerViewAdapter extends RecyclerView.Adapter<CustomerOrderRecyclerViewAdapter.ViewHolder> {
    private ArrayList<CartItem> order_items;
    private Context context;

    public CustomerOrderRecyclerViewAdapter(ArrayList<CartItem> order_items, Context context){
        this.order_items = order_items;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomerOrderRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item_card, parent, false);
        return new CustomerOrderRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerOrderRecyclerViewAdapter.ViewHolder holder, int position) {
        String orderItemId = order_items.get(position).getId();
        if(orderItemId.contains(Food.CODE)){
            Bind_Food(holder, position);
        }
        else if(orderItemId.contains(Drink.CODE)){
            Bind_Drink(holder, position);
        }
        holder.item_count_TextView.setText(String.valueOf(order_items.get(position).getQuantity()));
    }

    private void Bind_Food(CustomerOrderRecyclerViewAdapter.ViewHolder holder, int position){
        FoodHandler foodHandler = new FoodHandler();
        foodHandler.setIntoTextView(
                order_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        foodHandler.setIntoImageView(
                order_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    private void Bind_Drink(CustomerOrderRecyclerViewAdapter.ViewHolder holder, int position){
        DrinkHandler drinkHandler = new DrinkHandler();

        drinkHandler.setIntoTextView(
                order_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        drinkHandler.setIntoImageView(
                order_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    @Override
    public int getItemCount() {
        if(order_items==null){
            return 0;
        }
        return order_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView title_TextView, item_count_TextView;
        private ImageView image_ImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            InitViews(itemView);
        }

        private void InitViews(View view){
            title_TextView = view.findViewById(R.id.item_title_TextView);
            image_ImageView = view.findViewById(R.id.item_image_ImageView);
            item_count_TextView = view.findViewById(R.id.item_count_TextView);
        }
    }
}
