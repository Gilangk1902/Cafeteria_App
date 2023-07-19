package com.example.foodmenu.RecyclerViewAdapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.CartHandler;
import com.example.foodmenu.DataBaseHandler.DrinkHandler;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Fragments.CartFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

import java.util.ArrayList;
import java.util.List;

public class CartRecyclerViewAdapter extends RecyclerView.Adapter<CartRecyclerViewAdapter.ViewHolder>{
    private final String FOOD_CODE = "FD";
    private final String DRINK_CODE = "DK";

    private ArrayList<CartItem> cart_items = new ArrayList<>();
    private Context context;

    public CartRecyclerViewAdapter(ArrayList<CartItem> _cart_items, Context context){
        this.cart_items = _cart_items;
        this.context = context;
    }

    @NonNull
    @Override
    public CartRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                                    .inflate(R.layout.cart_item_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartRecyclerViewAdapter.ViewHolder holder, int position) {
        String cartItemId = cart_items.get(position).getId();
        if(cartItemId.contains(FOOD_CODE)){
            Bind_Food(holder, position);
        }
        else if(cartItemId.contains(DRINK_CODE)){
            Bind_Drink(holder, position);
        }
        holder.item_count_TextView.setText(String.valueOf(cart_items.get(position).getQuantity()));
    }

    private void Bind_Food(ViewHolder holder, int position){
        FoodHandler foodHandler = new FoodHandler();
        foodHandler.setIntoTextView(
                cart_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        foodHandler.setIntoTextView_Price(
                cart_items.get(position).getId(),
                cart_items.get(position).getQuantity(),
                holder.price_TextView
        );

        foodHandler.setIntoImageView(
                cart_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    private void Bind_Drink(ViewHolder holder, int position){
        DrinkHandler drinkHandler = new DrinkHandler();

        drinkHandler.setIntoTextView(
                cart_items.get(position).getId(),
                "name",
                holder.title_TextView
        );

        drinkHandler.setIntoTextView_Price(
                cart_items.get(position).getId(),
                cart_items.get(position).getQuantity(),
                holder.price_TextView
        );

        drinkHandler.setIntoImageView(
                cart_items.get(position).getId(),
                holder.image_ImageView,
                context
        );
    }

    @Override
    public int getItemCount() {
        if(cart_items == null){
            return 0;
        }
        else{
            return cart_items.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView price_TextView, title_TextView, item_count_TextView;
        private ImageView image_ImageView;
        private ConstraintLayout card_ConstraintLayout;
        private Button min_Button, plus_Button;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            InitViews(itemView);
            Listeners(itemView);
        }
        private void InitViews(View view){
            price_TextView = view.findViewById(R.id.item_price_TextView);
            title_TextView = view.findViewById(R.id.item_title_TextView);
            item_count_TextView = view.findViewById(R.id.item_count_TextView);

            image_ImageView = view.findViewById(R.id.item_image_ImageView);

            card_ConstraintLayout = view.findViewById(R.id.card_ConstraintLayout);
            min_Button = view.findViewById(R.id.minus_Button);
            plus_Button = view.findViewById(R.id.plus_Button);
        }

        public void Listeners(View itemView){
            CartHandler cartHandler = new CartHandler();
            plus_Button.setOnClickListener(v -> {
                if(cart_items.get(getAdapterPosition()).getQuantity()>=1){
                    //TODO
                    // add item
                    cartHandler.IncreaseQuantity(
                            Session.getUser().getId(),
                            cart_items.get(getAdapterPosition()).getId()
                    );
                    //refresh
                    FragmentUtils.ReplaceFragment(
                            ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager(),
                            R.id.user_FrameLayout,
                            new CartFragment()
                    );
                }
            });

            min_Button.setOnClickListener(v -> {
                cartHandler.DecreaseQuantity(
                        Session.getUser().getId(),
                        cart_items.get(getAdapterPosition()).getId()
                );
                //refresh
                FragmentUtils.ReplaceFragment(
                        ((AppCompatActivity)itemView.getContext()).getSupportFragmentManager(),
                        R.id.user_FrameLayout,
                        new CartFragment()
                );
            });
        }
    }
}
