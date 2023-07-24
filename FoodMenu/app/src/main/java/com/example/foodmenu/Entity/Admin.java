package com.example.foodmenu.Entity;

public class Admin extends User{
    public static String CODE = "AD";
    public Admin(String id, String name, String email, String password) {
        super(id, name, email, password);
    }
}
