package com.example.foodmenu.App_Start;

import com.example.foodmenu.Entity.Admin;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.Entity.User;

import java.util.ArrayList;

public class Session {
    public static final String CUSTOMER = "customer";
    public static final String ADMIN = "admin";

    private static User user;
    private static String role;

    public static void InitUser(){
        user = new User("CS001","Gilang","test123@gmail.com","test123");
        role = CUSTOMER;
    }

    public static void Login(User newUser, String _role){
        if(_role.equals(ADMIN)){
            user = new Admin(
                    newUser.getId(), newUser.getName(),
                    newUser.getEmail(), newUser.getPassword()
            );
            role = _role;
        }
        else if(_role.equals(CUSTOMER)){
            user = new Customer(
                    newUser.getId(), newUser.getName(),
                    newUser.getEmail(), newUser.getPassword()
            );
            role = _role;
        }
    }

    public static void Logout(){
        user = new User(null,null, null, null);
        role = "";
    }

    public static User getUser(){
        return user;
    }

    public static String getRole(){
        return role;
    }
}
