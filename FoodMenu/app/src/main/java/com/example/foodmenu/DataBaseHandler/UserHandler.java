package com.example.foodmenu.DataBaseHandler;

import com.google.firebase.database.DatabaseReference;

public class UserHandler {
    private DatabaseReference userReference;
    private DatabaseReference adminReference;

    public UserHandler(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        userReference = databaseSingleton.getDataBase().getReference().child("User");
        adminReference = databaseSingleton.getDataBase().getReference().child("User");
    }

    public DatabaseReference getCustomers(){
        return userReference.child("Customer");
    }

    public DatabaseReference getCustomerById(String id){
        return getCustomers().child("id");
    }

    public DatabaseReference getAdmins(){
        return adminReference.child("Admin");
    }
}
