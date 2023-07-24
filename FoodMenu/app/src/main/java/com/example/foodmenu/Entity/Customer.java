package com.example.foodmenu.Entity;

import com.example.foodmenu.DataBaseHandler.CustomerHandler;

import java.util.ArrayList;

public class Customer extends User{
    public static String CODE = "CS";
    public ArrayList<CartItem> cart = new ArrayList<>();

    public Customer(String id, String name, String email,
                    String password)
    {
        super(id, name, email, password);
        setCart(cart);
    }

    public ArrayList<CartItem> getCart() {
        return cart;
    }

    private void setCart(ArrayList<CartItem> cart) {
        CustomerHandler customerHandler = new CustomerHandler();
    }
}
