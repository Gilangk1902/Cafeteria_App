package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.Entity.CartItem;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Random;

public class OrderHandler {
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

    public void BindData_OrderId(){

    }

    private String GenerateOrderId(){
        String ORDER_CODE = "OD";
        Random random = new Random();

        String id_number = String.valueOf(random.nextInt(900)+100);

        return ORDER_CODE+id_number;
    }
}
