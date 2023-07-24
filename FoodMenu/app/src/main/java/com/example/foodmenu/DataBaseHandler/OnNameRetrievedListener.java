package com.example.foodmenu.DataBaseHandler;

import com.example.foodmenu.Entity.Order;

import java.util.ArrayList;

public interface OnNameRetrievedListener {
    void onNameRetrieved(String name, ArrayList<Order> orders);
}
