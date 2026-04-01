package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.RecyclerViewAdapters.CartRecyclerViewAdapter;
import com.google.firebase.database.*;

import java.util.ArrayList;

public class CartHandler {

    public static final String PLUS = "+";
    public static final String MINUS = "-";

    private final DatabaseReference cartReference;
    private ValueEventListener cartListener;

    public CartHandler() {
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
    }

    public void AddNewItem(String customerId, String itemId) {
        cartReference.child(customerId).child(itemId).child("quantity").setValue(1);
    }

    public void ModifyQuantity(String operator, String customerId, String itemId) {
        DatabaseReference quantityRef = cartReference
                .child(customerId)
                .child(itemId)
                .child("quantity");

        quantityRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer value = currentData.getValue(Integer.class);
                if (value == null) value = 0;

                if (operator.equals(MINUS) && value > 1) {
                    currentData.setValue(value - 1);
                } else if (operator.equals(PLUS)) {
                    currentData.setValue(value + 1);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                // Optional: callback
            }
        });
    }

    public void BindData(String customerId,
                         ArrayList<CartItem> cartItems,
                         ArrayList<Integer> prices,
                         RecyclerView recyclerView,
                         Context context,
                         OnDataBindCompleteListener callback) {

        if (cartListener != null) {
            cartReference.child(customerId).removeEventListener(cartListener);
        }

        cartListener = cartReference.child(customerId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                cartItems.clear();
                prices.clear();

                for (DataSnapshot cartSnap : snapshot.getChildren()) {
                    Integer qty = cartSnap.child("quantity").getValue(Integer.class);
                    if (qty == null) qty = 0;

                    cartItems.add(new CartItem(cartSnap.getKey(), qty));
                    prices.add(getPrice(cartSnap.getKey()) * qty);
                }

                if (cartItems.isEmpty()) {
                    Toast.makeText(context, "Cart is empty", Toast.LENGTH_SHORT).show();
                }

                CartRecyclerViewAdapter adapter = new CartRecyclerViewAdapter(
                        cartItems,
                        prices,
                        context,
                        callback
                );

                recyclerView.setLayoutManager(new GridLayoutManager(context, 1));
                recyclerView.setAdapter(adapter);

                callback.onDataBindComplete();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void removeListener(String customerId) {
        if (cartListener != null) {
            cartReference.child(customerId).removeEventListener(cartListener);
        }
    }

    private int getPrice(String itemId){
        // Replace with actual FoodHandler / DrinkHandler price lookup
        if(itemId.contains("FOOD")) return 20;
        else if(itemId.contains("DRINK")) return 10;
        return 0;
    }

    public DatabaseReference getCartReference() {
        return cartReference;
    }
}