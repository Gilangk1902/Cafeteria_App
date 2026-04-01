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

    private DatabaseReference cartReference;
    private ValueEventListener cartListener; // keep reference to avoid duplicates

    public CartHandler() {
        cartReference = FirebaseDatabase.getInstance().getReference().child("Cart");
    }

    public void AddNewItem(String customer_id, String itemId) {
        cartReference.child(customer_id).child(itemId).child("quantity").setValue(1);
    }

    public void ModifyQuantity(String operator, String customer_id, String itemId) {
        DatabaseReference quantityRef = cartReference
                .child(customer_id)
                .child(itemId)
                .child("quantity");

        quantityRef.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                Integer value = currentData.getValue(Integer.class);

                if (value == null) value = 0;

                if (operator.equals(MINUS)) {
                    if (value > 1) {
                        currentData.setValue(value - 1);
                    }
                } else if (operator.equals(PLUS)) {
                    currentData.setValue(value + 1);
                }

                return Transaction.success(currentData);
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                // Optional: handle result
            }
        });
    }

    // ✅ FIXED: Real-time binding
    public void Bind_Data(String customer_id,
                          ArrayList<Integer> prices,
                          RecyclerView recyclerView,
                          Context context,
                          OnDataBindCompleteListener callback) {

        // Remove old listener to prevent duplicates
        if (cartListener != null) {
            cartReference.child(customer_id).removeEventListener(cartListener);
        }

        cartListener = cartReference.child(customer_id)
                .addValueEventListener(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                        ArrayList<CartItem> cartItems = new ArrayList<>();

                        for (DataSnapshot cartSnap : snapshot.getChildren()) {
                            Integer qty = cartSnap.child("quantity").getValue(Integer.class);

                            if (qty == null) qty = 0;

                            cartItems.add(new CartItem(
                                    cartSnap.getKey(),
                                    qty
                            ));
                        }

                        if (cartItems.size() <= 0) {
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
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // ✅ Optional: call this in Activity onDestroy()
    public void removeListener(String customer_id) {
        if (cartListener != null) {
            cartReference.child(customer_id).removeEventListener(cartListener);
        }
    }

    public DatabaseReference getCartReference() {
        return cartReference;
    }
}