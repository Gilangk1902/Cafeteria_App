package com.example.foodmenu.RecyclerViewAdapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmenu.Activity.FoodActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.R;

import java.util.ArrayList;

public class FoodRecyclerViewAdapter extends RecyclerView.Adapter<FoodRecyclerViewAdapter.ViewHolder> {
    private ArrayList<Food> foods = new ArrayList<>();
    private Context context;
    public FoodRecyclerViewAdapter(ArrayList<Food> foods, Context context){
        this.foods = foods;
        this.context = context;
    }

    @NonNull
    @Override
    public FoodRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                .inflate(R.layout.item_card_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodRecyclerViewAdapter.ViewHolder holder, int position) {
        Glide.with(context)
                        .load(foods.get(position).getImageUrl())
                        .into(holder.image_ImageView);
        holder.title_TextView.setText(foods.get(position).getName());
        holder.price_TextView.setText("Rp. " + foods.get(position).getPrice());

    }

    @Override
    public int getItemCount() {
        if(foods == null){
            return 0;
        }
        else{
            return foods.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price_TextView, title_TextView;
        private ImageView image_ImageView;
        private ConstraintLayout card_ConstraintLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            InitViews(itemView);
            Listeners();
        }
        private void InitViews(View view){
            price_TextView = view.findViewById(R.id.item_price_TextView);
            title_TextView = view.findViewById(R.id.item_title_TextView);

            image_ImageView = view.findViewById(R.id.item_image_ImageView);

            card_ConstraintLayout = view.findViewById(R.id.card_ConstraintLayout);
        }

        private void Listeners(){
            card_ConstraintLayout.setOnClickListener(v -> {
                AppCompatActivity activity =  (AppCompatActivity) context;
                if(Session.getUser().getName()!=null && Session.getUser().getName()!=""){
                    Intent intent = new Intent(context, FoodActivity.class);
                    intent.putExtra(FoodActivity.DETAIL_KEY, foods.get(getAdapterPosition()));
                    context.startActivity(intent);
                }
            });
        }
    }
}
