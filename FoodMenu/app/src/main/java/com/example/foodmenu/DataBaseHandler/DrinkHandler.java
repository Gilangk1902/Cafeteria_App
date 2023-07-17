package com.example.foodmenu.DataBaseHandler;

import android.net.Uri;

import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Factory.DrinkFactory;
import com.example.foodmenu.Factory.FoodFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class DrinkHandler {
    private DatabaseReference drinkDatabaseReference;
    private StorageReference drinkStorageReference;

    public DrinkHandler(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        drinkDatabaseReference = databaseSingleton.getDataBase().getReference().child("Drink");
        drinkStorageReference = databaseSingleton.getStorage().getReference().child("Drink");
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

                    DatabaseReference drinkReference = getDrinkDatabaseReference().child(drink.getId());
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

                    DatabaseReference drinkReference = getDrinkDatabaseReference().child(target_id);
                    drinkReference.child("name").setValue(name);
                    drinkReference.child("price").setValue(price);
                    drinkReference.child("image").setValue(image_Url);
                }).addOnFailureListener(e->{
                    //handle failure
                });
            });
        }
        else{
            DatabaseReference drinkReference = getDrinkDatabaseReference().child(target_id);
            drinkReference.child("name").setValue(name);
            drinkReference.child("price").setValue(price);
        }
    }

    public DatabaseReference getDrinkDatabaseReference(){
        return drinkDatabaseReference;
    }

    public StorageReference getDrinkStorageReference(){
        return drinkStorageReference;
    }
}
