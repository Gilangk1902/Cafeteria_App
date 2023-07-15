package com.example.foodmenu.DataBaseHandler;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

public class FoodHandler {
    private DatabaseReference foodDatabaseReference;
    private StorageReference foodStorageReference;
    public FoodHandler(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        foodDatabaseReference = databaseSingleton.getDataBase().getReference().child("Food");
        foodStorageReference = databaseSingleton.getStorage().getReference().child("Food");
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
