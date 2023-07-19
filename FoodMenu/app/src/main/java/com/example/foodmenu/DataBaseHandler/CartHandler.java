package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.RecyclerViewAdapters.CartRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartHandler implements Ihandler{
    private DatabaseReference cartReference;

    public CartHandler(){
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
    }

    public void AddNewItem(String customer_id, String itemId){
        getCartReference().child(customer_id).child(itemId).child("quantity").setValue(1);
    }

    public void IncreaseQuantity(String customer_id, String itemId){
        DatabaseReference quantity_reference = getCartReference().child(customer_id).child(itemId)
                                                .child("quantity");

        quantity_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Increment(quantity_reference, snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void DecreaseQuantity(String customer_id, String itemId){
        DatabaseReference quantity_reference = getCartReference().child(customer_id).child(itemId)
                .child("quantity");

        quantity_reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Decrement(quantity_reference, snapshot.getValue(Integer.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void Increment(DatabaseReference databaseReference, int current_quantity){
        databaseReference.setValue(current_quantity + 1);
    }
    private void Decrement(DatabaseReference databaseReference, int current_quantity){
        databaseReference.setValue(current_quantity - 1);
    }

    public void Bind_Data(String customer_id,RecyclerView cart_RecyclerView, Context context){
        getCartReference().child(customer_id).addListenerForSingleValueEvent(new ValueEventListener() {
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
                if(cartItems.size()<=0){
                    Toast.makeText(context, "failed to retrieve", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(context, "you have " + cartItems.size() +
                                    " items in your cart!", Toast.LENGTH_SHORT).show();
                }
                CartRecyclerViewAdapter cartRecyclerViewAdapter = new CartRecyclerViewAdapter(
                        cartItems, context
                );
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 1);
                cart_RecyclerView.setLayoutManager(gridLayoutManager);
                cart_RecyclerView.setAdapter(cartRecyclerViewAdapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, "peler", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public DatabaseReference getCartReference() {
        return cartReference;
    }

    public void setCartReference(DatabaseReference cartReference) {
        this.cartReference = cartReference;
    }
}
