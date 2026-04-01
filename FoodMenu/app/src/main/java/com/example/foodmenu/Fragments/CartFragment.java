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
import com.example.foodmenu.DataBaseHandler.DrinkHandler;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.DataBaseHandler.OnDataBindCompleteListener;
import com.example.foodmenu.DataBaseHandler.OrderHandler;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.R;
import com.example.foodmenu.RecyclerViewAdapters.CartRecyclerViewAdapter;
import com.example.foodmenu.Utils.FragmentUtils;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartFragment extends Fragment implements OnDataBindCompleteListener {

    private RecyclerView cart_recyclerView;
    private TextView totalPrice_TextView;
    private Button order_Button, back_Button;

    private ArrayList<Integer> prices = new ArrayList<>();
    private ArrayList<CartItem> cartItems = new ArrayList<>();
    public CartFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitViews(view);
        Listeners();

        CartHandler cartHandler = new CartHandler();

        if(Session.getUser().getId().contains(Customer.CODE)){
            cartHandler.BindData(
                    Session.getUser().getId(),
                    cartItems,       // pass this
                    prices,          // and this
                    cart_recyclerView,
                    getContext(),
                    this
            );
        }
    }

    private void InitViews(View view){
        cart_recyclerView = view.findViewById(R.id.cart_RecyclerView);
        order_Button = view.findViewById(R.id.order_Button);
        totalPrice_TextView = view.findViewById(R.id.total_price_TextView);
        back_Button = view.findViewById(R.id.back_Button);
    }

    private void Listeners(){

        order_Button.setOnClickListener(v -> {
            if(order_Button.getText().equals("Done")){
//                totalPrice_TextView.setVisibility(View.VISIBLE);
//                setTotalPrice();
//                order_Button.setText("Order");
                Order();
            }
            else if(order_Button.getText().equals("Order")){
                Order();
            }
        });

        back_Button.setOnClickListener(v -> {
//            requireActivity().onBackPressed();

            FragmentUtils.ReplaceFragment(
                    getParentFragmentManager(), R.id.user_FrameLayout, new ShowAllFragment()
            );
        });
    }

    private void setTotalPrice(){
        int total_price = 0;
        for(Integer price : prices){
            total_price += price;
        }
        totalPrice_TextView.setText("Total price : " + total_price);
    }

    private void Order(){
        OrderHandler orderHandler = new OrderHandler();
        CartHandler cartHandler = new CartHandler();

        cartHandler.getCartReference()
                .child(Session.getUser().getId())
                .addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ArrayList<CartItem> cartItems = new ArrayList<>();

                        for(DataSnapshot cart_snapShot : snapshot.getChildren()){
                            CartItem cartItem = new CartItem(
                                    cart_snapShot.getKey(),
                                    cart_snapShot.child("quantity").getValue(Integer.class)
                            );
                            cartItems.add(cartItem);
                        }

                        orderHandler.OrderCart(
                                Session.getUser().getId(),
                                cartItems,
                                getContext()
                        );
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {}
                });
    }

    @Override
    public void onDataBindComplete() {
        CartRecyclerViewAdapter adapter =
                (CartRecyclerViewAdapter) cart_recyclerView.getAdapter();

        if (adapter == null) return;

        int total = 0;

        for (Integer price : adapter.getItemTotals()) {
            total += price;
        }

        totalPrice_TextView.setText("Total price: " + total);
    }
}