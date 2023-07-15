package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.content.Intent;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

public class UserHandler {
    private DatabaseReference userReference;
    private DatabaseReference adminReference;

    public UserHandler(){
        DatabaseSingleton databaseSingleton = new DatabaseSingleton();
        userReference = databaseSingleton.getDataBase().getReference().child("User");
        adminReference = databaseSingleton.getDataBase().getReference().child("User");
    }

    public void Register(User newUser){
        DatabaseReference userReference = getCustomersDatabaseReference().child(newUser.getId());
        userReference.child("name").setValue(newUser.getName());
        userReference.child("email").setValue(newUser.getEmail());
        userReference.child("password").setValue(newUser.getPassword());
    }

    public void Login(DataSnapshot customerSnapShot, String email, String password, String role, Context context){
        if(customerSnapShot.child("email")
                .getValue(String.class).equals(email)){
            if(customerSnapShot.child("password")
                    .getValue(String.class).equals(password)){
                String _email, _password, _name;
                _name = customerSnapShot.child("name").getValue(String.class);
                _email = customerSnapShot.child("email").getValue(String.class);
                _password = customerSnapShot.child("password").getValue(String.class);

                Session.Login(
                        new User(customerSnapShot.getKey(), _name, _email, _password),
                        role
                );

                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            }
            else{
                //Password Doesn't Match
            }
        }
    }

    public DatabaseReference getCustomersDatabaseReference(){
        return userReference.child("Customer");
    }

    public DatabaseReference getCustomerById(String id){
        return getCustomersDatabaseReference().child("id");
    }

    public DatabaseReference getAdmins(){
        return adminReference.child("Admin");
    }
}
