package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.Entity.Item;
import com.example.foodmenu.Entity.Order;
import com.example.foodmenu.Fragments.CustomerOrderFragment;
import com.example.foodmenu.RecyclerViewAdapters.CustomerOrderRecyclerViewAdapter;
import com.example.foodmenu.RecyclerViewAdapters.OrderRecyclerViewAdapter;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Random;

public class OrderHandler{
    private DatabaseReference user_cart_reference;
    private DatabaseReference order_reference;

    public OrderHandler(){
        user_cart_reference = FirebaseDatabase.getInstance().getReference().child("Cart");
        order_reference = FirebaseDatabase.getInstance().getReference().child("Order");
    }

    public void OrderCart(String customer_id ,ArrayList<CartItem> cartItems, Context context){
        DatabaseReference newOrder_reference = order_reference.child(GenerateOrderId())
                .child(customer_id);

        for(CartItem item : cartItems){
            newOrder_reference.child(item.getId()).child("quantity").setValue(item.getQuantity());
            newOrder_reference.child(item.getId()).child("status").setValue("waiting");
        }

        user_cart_reference.child(customer_id).removeValue().addOnSuccessListener(unused -> {
            Intent intent = new Intent(context, MainActivity.class);
            context.startActivity(intent);
        });
    }

    public void BindData_AllOrder(RecyclerView order_recyclerView, Context context){
        getOrder_reference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Order> orders = new ArrayList<>();
                for(DataSnapshot order_snapshot : snapshot.getChildren()){
                    for(DataSnapshot customer_snapshot : order_snapshot.getChildren()){
                        orders.add(
                                new Order(order_snapshot.getKey(),
                                        customer_snapshot.getKey()
                                )
                        );
                    }
                }
                OrderRecyclerViewAdapter orderRecyclerViewAdapter = new OrderRecyclerViewAdapter(
                        orders, context
                );
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                order_recyclerView.setLayoutManager(gridLayoutManager);
                order_recyclerView.setAdapter(orderRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void BindData_CustomerOrder(String id ,RecyclerView order_recyclerView, Context context){
        getOrder_reference().child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<CartItem> orderItems = new ArrayList<>();
                for(DataSnapshot customer_snapshot : snapshot.getChildren()){
                    for (DataSnapshot item_snapshot : customer_snapshot.getChildren()){
                        orderItems.add(
                                new CartItem(
                                        item_snapshot.getKey(),
                                        item_snapshot.child("quantity").getValue(Integer.class)
                                )
                        );
                    }
                }
                CustomerOrderRecyclerViewAdapter orderRecyclerViewAdapter =
                new CustomerOrderRecyclerViewAdapter(
                        orderItems, context
                );
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                order_recyclerView.setLayoutManager(gridLayoutManager);
                order_recyclerView.setAdapter(orderRecyclerViewAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private String GenerateOrderId(){
        String ORDER_CODE = "OD";
        Random random = new Random();

        String id_number = String.valueOf(random.nextInt(900)+100);

        return ORDER_CODE+id_number;
    }

    public DatabaseReference getUser_cart_reference() {
        return user_cart_reference;
    }

    public DatabaseReference getOrder_reference() {
        return order_reference;
    }

}
