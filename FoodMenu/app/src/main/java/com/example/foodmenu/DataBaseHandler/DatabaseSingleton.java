package com.example.foodmenu.DataBaseHandler;

import com.example.foodmenu.Entity.Food;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class DatabaseSingleton {
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    public DatabaseSingleton(){
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
    }

    public FirebaseDatabase getDataBase(){
        return database;
    }

    public FirebaseStorage getStorage(){
        return storage;
    }
}
