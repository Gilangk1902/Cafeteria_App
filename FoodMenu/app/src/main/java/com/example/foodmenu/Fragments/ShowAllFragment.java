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

        FoodHandler foodHandler = new FoodHandler();
        foodHandler.Bind_Data("",item_RecyclerView, getContext());
    }

    private void InitViews(View view){
        foods_Button = view.findViewById(R.id.foods_Button);
        drinks_Button = view.findViewById(R.id.drinks_Button);
        item_RecyclerView = view.findViewById(R.id.food_RecyclerView);
    }

    private void Listeners(View view){
        foods_Button.setOnClickListener(v -> {
            FoodHandler foodHandler = new FoodHandler();
            foodHandler.Bind_Data("",item_RecyclerView, getContext());
        });
        drinks_Button.setOnClickListener(v -> {
            DrinkHandler drinkHandler = new DrinkHandler();
            drinkHandler.Bind_Data("",item_RecyclerView, getContext());
        });
    }


}