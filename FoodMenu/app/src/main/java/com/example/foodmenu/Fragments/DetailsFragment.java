package com.example.foodmenu.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.foodmenu.Activity.UserActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.CartHandler;
import com.example.foodmenu.Entity.Item;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

public class DetailsFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Item item;

    private ImageView item_image;
    private Button buy_button;
    private TextView item_name, item_price;
    public DetailsFragment(Item item) {
        this.item = item;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_food_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        Listeners();
    }

    private void InitViews(View view){
        item_image = view.findViewById(R.id.item_image_ImageView);
        item_name = view.findViewById(R.id.item_name_title_TextView);
        item_price = view.findViewById(R.id.item_price_TextView);
        buy_button = view.findViewById(R.id.buy_Button);

        if(item !=null){
            BindData();
        }

        if(Session.getRole().equals("admin")){
            buy_button.setText("Edit");
        }
        else if(Session.getRole().equals("customer")){
            buy_button.setText("buy");
        }
    }

    private void Listeners(){
        if(Session.getRole().equals("admin")){
            buy_button.setOnClickListener(v->{
                FragmentUtils.ReplaceFragment(getParentFragmentManager(),
                        R.id.foodActivity_FrameLayout, new EditFragment(item));
            });
        }
        else if(Session.getRole().equals("customer")){
            buy_button.setOnClickListener(v->{
                CartHandler cartHandler = new CartHandler();
                cartHandler.AddNewItem(Session.getUser().getId(), item.getId());

                Intent intent = new Intent(getContext(), UserActivity.class);
                intent.putExtra(UserActivity.CART_KEY, 1);
                startActivity(intent);
            });
        }
    }

    private void BindData(){
        item_name.setText(item.getName());
        item_price.setText("Rp. "+ item.getPrice());
        Glide.with(getContext()).load(item.getImageUrl()).into(item_image);
    }
}