package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Factory.FoodFactory;
import com.example.foodmenu.RecyclerViewAdapters.FoodRecyclerViewAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.*;

import java.util.ArrayList;

public class FoodHandler implements Ihandler, IhandlerUtils{
    private DatabaseReference databaseReference;
    private StorageReference foodStorageReference;

    public FoodHandler(){
        databaseReference = FirebaseDatabase.getInstance().getReference().child("Food");
        foodStorageReference = FirebaseStorage.getInstance().getReference().child("Food");
    }

    public void Add(String name, String price,Uri image_Uri){
        if(image_Uri!=null){
            StorageReference imageRef = getFoodStorageReference()
                    .child(name+".jpg");

            UploadTask uploadTask = imageRef.putFile(image_Uri);
            uploadTask.addOnSuccessListener(taskSnapshot ->
            {
                imageRef.getDownloadUrl().addOnSuccessListener(uri ->
                {
                    String image_Url = uri.toString();

                    Food food = FoodFactory.Create(name, price, image_Url);

                    DatabaseReference foodReference = getDatabaseReference().child(food.getId());
                    foodReference.child("name").setValue(food.getName());
                    foodReference.child("price").setValue(food.getPrice());
                    foodReference.child("image").setValue(food.getImageUrl());
                });
            }).addOnFailureListener(e -> {
                //handle failure
            });
        }
    }

    public void Update(String target_id,String name, String price, Uri image_Uri){
        if(image_Uri!=null){
            StorageReference imageRef = getFoodStorageReference().child(name+".jpg");

            UploadTask uploadTask = imageRef.putFile(image_Uri);
            uploadTask.addOnSuccessListener(taskSnapshot -> {
                imageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                    String image_Url  = uri.toString();

                    DatabaseReference foodReference = getDatabaseReference().child(target_id);
                    foodReference.child("name").setValue(name);
                    foodReference.child("price").setValue(price);
                    foodReference.child("image").setValue(image_Url);
                }).addOnFailureListener(e->{
                    //handle failure
                });
            });
        }
        else{
            DatabaseReference foodReference = getDatabaseReference().child(target_id);
            foodReference.child("name").setValue(name);
            foodReference.child("price").setValue(price);
        }
    }

    public void Bind_Data(String id,RecyclerView item_RecyclerView, Context context){
        getDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<Food> foods = new ArrayList<>();
                for(DataSnapshot food_snapshot : snapshot.getChildren()){
                    Food newFood = new Food(
                            food_snapshot.getKey(),
                            food_snapshot.child("name").getValue(String.class),
                            food_snapshot.child("price").getValue(String.class),
                            food_snapshot.child("image").getValue(String.class)
                    );

                    foods.add(newFood);
                }

                FoodRecyclerViewAdapter food_recyclerview_adapter  = new FoodRecyclerViewAdapter(
                        foods, context
                );
                GridLayoutManager gridLayoutManager = new GridLayoutManager(context, 2);
                item_RecyclerView.setLayoutManager(gridLayoutManager);
                item_RecyclerView.setAdapter(food_recyclerview_adapter);
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
                    String text = snapshot.child(key).getValue(String.class);
                    textView.setText(text);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void setIntoTextView_Price(String id, int quantity, TextView price_TextView,
                                      ArrayList<Integer> prices, OnDataBindCompleteListener callback){
        databaseReference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String price_inString = snapshot.child("price").getValue(String.class);
                    int price_inInteger = Integer.parseInt(price_inString);
                    prices.add(price_inInteger*quantity);
                    price_TextView.setText("Rp. " + String.valueOf(price_inInteger*quantity));
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

    public DatabaseReference getFoodById(String id){
        return databaseReference.child(id);
    }

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public StorageReference getFoodStorageReference(){
        return foodStorageReference;
    }

}
