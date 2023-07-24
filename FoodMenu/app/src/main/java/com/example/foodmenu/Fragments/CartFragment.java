package com.example.foodmenu.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.CartHandler;
import com.example.foodmenu.DataBaseHandler.OnDataBindCompleteListener;
import com.example.foodmenu.DataBaseHandler.OrderHandler;
import com.example.foodmenu.Entity.Admin;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment implements OnDataBindCompleteListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private RecyclerView cart_recyclerView;
    private TextView totalPrice_TextView;
    private Button order_Button;

    private String CODE="";

    private ArrayList<Integer> prices = new ArrayList<>();

    public CartFragment() {
        // Required empty public constructor
    }

    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitViews(view);

        Listeners();

        CartHandler cartHandler = new CartHandler();
        if(Session.getUser().getId().contains(Customer.CODE)){
            cartHandler.Bind_Data(
                    Session.getUser().getId(), prices,cart_recyclerView, getContext(), this
            );
        }
    }

    private void setTotalPrice(){
        int total_price = 0;
        for(Integer price : prices){
            total_price+=price;
        }
        totalPrice_TextView.setText("total price : " + String.valueOf(total_price));
    }

    private void Listeners(){
        order_Button.setOnClickListener(v -> {
            if(order_Button.getText().equals("Done")){
                totalPrice_TextView.setVisibility(View.VISIBLE);
                setTotalPrice();
                order_Button.setText("Order");
            }
            else if(order_Button.getText().equals("Order")){
                Order();
            }
        });
    }

    private void Order(){
        OrderHandler orderHandler = new OrderHandler();
        CartHandler cartHandler = new CartHandler();

        cartHandler.getCartReference().child(Session.getUser().getId())
                .addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        ArrayList<CartItem> cartItems = new ArrayList<CartItem>();
                        for(DataSnapshot cart_snapShot : snapshot.getChildren()){
                            CartItem cartItem = new CartItem(
                                    cart_snapShot.getKey(),
                                    cart_snapShot.child("quantity").getValue(Integer.class)
                            );
                            cartItems.add(cartItem);
                        }

                        orderHandler.OrderCart(
                                Session.getUser().getId(), cartItems, getContext()
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    private void InitViews(View view){
        cart_recyclerView = view.findViewById(R.id.cart_RecyclerView);
        order_Button = view.findViewById(R.id.order_Button);
        totalPrice_TextView = view.findViewById(R.id.total_price_TextView);
    }

    @Override
    public void onDataBindComplete() {
        setTotalPrice();
        Toast.makeText(getContext(), "complete", Toast.LENGTH_SHORT).show();
    }
}