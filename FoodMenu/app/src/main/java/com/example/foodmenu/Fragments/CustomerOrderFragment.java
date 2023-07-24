package com.example.foodmenu.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.foodmenu.DataBaseHandler.OrderHandler;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Item;
import com.example.foodmenu.R;

import java.util.ArrayList;

public class CustomerOrderFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ArrayList<CartItem> items = new ArrayList<>();
    private Context context;

    private RecyclerView items_RecyclerView;
    private String id;

    public CustomerOrderFragment(String id, Context _context) {
        this.context = _context;
        this.id = id;
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_order, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
    }

    private void InitViews(View view){
        items_RecyclerView = view.findViewById(R.id.item_RecyclerView);
        OrderHandler orderHandler = new OrderHandler();
        orderHandler.BindData_CustomerOrder(this.id, items_RecyclerView,getContext());
    }


}