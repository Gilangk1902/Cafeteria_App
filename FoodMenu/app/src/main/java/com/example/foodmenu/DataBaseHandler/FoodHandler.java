package com.example.foodmenu.DataBaseHandler;

import android.net.Uri;
import android.widget.Toast;

import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Factory.FoodFactory;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class FoodHandler {
    private DatabaseReference foodDatabaseReference;
    private StorageReference foodStorageReference;
    public FoodHandler(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        foodDatabaseReference = databaseSingleton.getDataBase().getReference().child("Food");
        foodStorageReference = databaseSingleton.getStorage().getReference().child("Food");
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

                    DatabaseReference foodReference = getFoodDatabaseReference().child(food.getId());
                    foodReference.child("name").setValue(food.getName());
                    foodReference.child("price").setValue(food.getPrice());
                    foodReference.child("image").setValue(food.getImageUrl());
                });
            }).addOnFailureListener(e -> {
                //handle failure
            });
        }
    }

    public DatabaseReference getFoodDatabaseReference(){
        return foodDatabaseReference;
    }

    public StorageReference getFoodStorageReference(){
        return foodStorageReference;
    }

    public DatabaseReference getFoodById(String id){
        return foodDatabaseReference.child(id);
    }
}
