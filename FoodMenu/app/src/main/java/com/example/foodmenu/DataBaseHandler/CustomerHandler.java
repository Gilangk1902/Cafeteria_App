package com.example.foodmenu.DataBaseHandler;

import android.content.Context;
import android.content.Intent;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Entity.CartItem;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.Entity.Order;
import com.example.foodmenu.Entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CustomerHandler implements Ihandler{
    private DatabaseReference customerReference;

    public CustomerHandler(){
        customerReference = FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child("User").child("Customer");
    }

    public void Register(User newUser){
        DatabaseReference customerReference = getCustomersDatabaseReference().child(newUser.getId());
        customerReference.child("name").setValue(newUser.getName());
        customerReference.child("email").setValue(newUser.getEmail());
        customerReference.child("password").setValue(newUser.getPassword());
    }

    public void Login(String email, String password, Context context){
        getCustomersDatabaseReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot customerSnapShot : snapshot.getChildren()){
                    if(customerSnapShot.child("email").getValue(String.class).equals(email)){
                        if(customerSnapShot.child("password").getValue(String.class).equals(password)){
                            String _email, _password, _name;
                            _name = customerSnapShot.child("name").getValue(String.class);
                            _email = customerSnapShot.child("email").getValue(String.class);
                            _password = customerSnapShot.child("password").getValue(String.class);

                            Session.Login(
                                    new Customer(
                                            customerSnapShot.getKey(),
                                            _name,
                                            _email,
                                            _password
                                            ),
                                    Session.CUSTOMER
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

    public void getCustomerNameByID(String id, ArrayList<Order> orders ,final OnNameRetrievedListener listener){
        getCustomersDatabaseReference().child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String name = snapshot.child("name").getValue(String.class);
                    if(name!=null){
                        listener.onNameRetrieved(name, orders);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public DatabaseReference getCustomersDatabaseReference(){
        return customerReference;
    }

    @Override
    public void Bind_Data(String id, RecyclerView recyclerView, Context contex) {

    }
}
