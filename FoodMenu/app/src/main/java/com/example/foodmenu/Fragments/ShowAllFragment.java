package com.example.foodmenu.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.foodmenu.DataBaseHandler.DrinkHandler;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.R;
import com.example.foodmenu.RecyclerViewAdapters.DrinkRecyclerViewAdapter;
import com.example.foodmenu.RecyclerViewAdapters.FoodRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShowAllFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShowAllFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button foods_Button, drinks_Button;

    private RecyclerView item_RecyclerView;
    private FoodRecyclerViewAdapter food_recyclerview_adapter;
    private DrinkRecyclerViewAdapter drink_recyclerview_adapter;


    public ShowAllFragment() {
        // Required empty public constructor
    }

    public static ShowAllFragment newInstance() {
        ShowAllFragment fragment = new ShowAllFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
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
        return inflater.inflate(R.layout.fragment_show_all, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        Listeners(view);
        Bind_FoodData(view);
    }

    private void InitViews(View view){
        foods_Button = view.findViewById(R.id.foods_Button);
        drinks_Button = view.findViewById(R.id.drinks_Button);
        item_RecyclerView = view.findViewById(R.id.food_RecyclerView);
    }

    private void Listeners(View view){
        foods_Button.setOnClickListener(v -> {
            Bind_FoodData(view);
        });
        drinks_Button.setOnClickListener(v -> {
            Bind_DrinkData(view);
        });
    }

    private void Bind_FoodData(View view){
        FoodHandler foodHandler = new FoodHandler();
        foodHandler.getFoodDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> foods = new ArrayList<>();
                for(DataSnapshot food_snapshot : snapshot.getChildren()){
                    Food newFood = new Food(food_snapshot.getKey(),
                            food_snapshot.child("name").getValue(String.class),
                            food_snapshot.child("price").getValue(String.class),
                            food_snapshot.child("image").getValue(String.class));

                    foods.add(newFood);
                }

                food_recyclerview_adapter  = new FoodRecyclerViewAdapter(foods, getContext());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                item_RecyclerView.setLayoutManager(gridLayoutManager);
                item_RecyclerView.setAdapter(food_recyclerview_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Bind_DrinkData(View view){
        DrinkHandler drinkHandler = new DrinkHandler();
        drinkHandler.getDrinkDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Drink> drinks = new ArrayList<>();
                for(DataSnapshot drink_snapshot : snapshot.getChildren()){
                    Drink newDrink = new Drink(drink_snapshot.getKey(),
                            drink_snapshot.child("name").getValue(String.class),
                            drink_snapshot.child("price").getValue(String.class),
                            drink_snapshot.child("image").getValue(String.class));

                    drinks.add(newDrink);
                }

                drink_recyclerview_adapter  = new DrinkRecyclerViewAdapter(drinks, getContext());
                GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
                item_RecyclerView.setLayoutManager(gridLayoutManager);
                item_RecyclerView.setAdapter(drink_recyclerview_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}