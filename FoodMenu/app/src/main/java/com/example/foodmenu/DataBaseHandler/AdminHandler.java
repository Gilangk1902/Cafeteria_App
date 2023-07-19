package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AdminHandler {
    private DatabaseReference adminReference;

    public AdminHandler(){
        FirebaseDatabase.getInstance().getReference().child("User").child("Admin");
    }

    public void Register(User newUser){
        DatabaseReference adminReference = getAdminDatabaseReference().child(newUser.getId());
        adminReference.child("name").setValue(newUser.getName());
        adminReference.child("email").setValue(newUser.getEmail());
        adminReference.child("password").setValue(newUser.getPassword());
    }

    public void Login(String email, String password, Context context)
    {
        getAdminDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot adminSnapShot : snapshot.getChildren()){
                    if(adminSnapShot.child("email").getValue(String.class).equals(email)){
                        if(adminSnapShot.child("password").getValue(String.class).equals(password)){
                            String _email, _password, _name;
                            _name = adminSnapShot.child("name").getValue(String.class);
                            _email = adminSnapShot.child("email").getValue(String.class);
                            _password = adminSnapShot.child("password").getValue(String.class);

                            Session.Login(
                                    new User(adminSnapShot.getKey(), _name, _email, _password),
                                    Session.ADMIN
                            );

                            Intent intent = new Intent(context, MainActivity.class);
                            context.startActivity(intent);
                        }
                        else{
                            //Password Doesn't Match
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public DatabaseReference getAdminDatabaseReference(){
        return adminReference;
    }
}
