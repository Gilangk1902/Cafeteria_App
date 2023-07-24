package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Factory.DrinkFactory;
import com.example.foodmenu.RecyclerViewAdapters.DrinkRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.util.ArrayList;

public class DrinkHandler implements Ihandler,IhandlerUtils{
    public DatabaseReference databaseReference;
    private StorageReference drinkStorageReference;

    public DrinkHandler(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Drink");
        drinkStorageReference = FirebaseStorage.getInstance().getReference().child("Drink");
    }

    public void Add(String name, String price, Uri image_Uri){
        if(image_Uri!=null){
            StorageReference imageRef = getDrinkStorageReference()
                    .child(name+".jpg");

            UploadTask uploadTask = imageRef.putFile(image_Uri);
            uploadTask.addOnSuccessListener(taskSnapshot ->
            {
                imageRef.getDownloadUrl().addOnSuccessListener(uri ->
                {
                    String image_Url = uri.toString();

                    Drink drink = DrinkFactory.Create(name, price, image_Url);

                    DatabaseReference drinkReference = getDatabaseReference().child(drink.getId());
                    drinkReference.child("name").setValue(drink.getName());
                    drinkReference.child("price").setValue(drink.getPrice());
                    drinkReference.child("image").setValue(drink.getImageUrl());
                });
            }).addOnFailureListener(e -> {
                //handle failure
            });
        }
    }

    public void Update(String target_id,String name, String price, Uri image_Uri){
        if(image_Uri!=null){
            StorageReference imageRef = getDrinkStorageReference().child(name+".jpg");

            UploadTask uploadTask = imageRef.putFile(image_Uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String image_Url  = uri.toString();

                    DatabaseReference drinkReference = getDatabaseReference().child(target_id);
                    drinkReference.child("name").setValue(name);
                    drinkReference.child("price").setValue(price);
                    drinkReference.child("image").setValue(image_Url);
                }).addOnFailureListener(e->{
                    //handle failure
                });
            });
        }
        else{
            DatabaseReference drinkReference = getDatabaseReference().child(target_id);
            drinkReference.child("name").setValue(name);
            drinkReference.child("price").setValue(price);
        }
    }

    public void Bind_Data(String id,RecyclerView item_RecyclerView, Context context){
        getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Drink> drinks = new ArrayList<>();
                for(DataSnapshot drink_snapshot : snapshot.getChildren()){
                    Drink newDrink = new Drink(drink_snapshot.getKey(),
                            drink_snapshot.child("name").getValue(String.class),
                            drink_snapshot.child("price").getValue(String.class),
                            drink_snapshot.child("image").getValue(String.class)
                    );

                    drinks.add(newDrink);
                }

                DrinkRecyclerViewAdapter drink_recyclerview_adapter  = new DrinkRecyclerViewAdapter(
                        drinks, context
                );
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                item_RecyclerView.setLayoutManager(gridLayoutManager);
                item_RecyclerView.setAdapter(drink_recyclerview_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setIntoTextView(String id, String key, TextView textView) {
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    textView.setText(
                            snapshot.child(key).getValue(String.class)
                    );
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setIntoTextView_Price(String id, int quantity, TextView textView,
                                      ArrayList<Integer> prices, OnDataBindCompleteListener callback){
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String price_inString = snapshot.child("price").getValue(String.class);
                    int price_inInteger = Integer.parseInt(price_inString);
                    prices.add(price_inInteger*quantity);
                    textView.setText("Rp. " + String.valueOf(price_inInteger*quantity));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setIntoImageView(String id, ImageView imageView, Context context){
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String imageUrl = snapshot.child("image").getValue(String.class);
                    Glide.with(context).load(imageUrl).into(imageView);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public StorageReference getDrinkStorageReference(){
        return drinkStorageReference;
    }
}
