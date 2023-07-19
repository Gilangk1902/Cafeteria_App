package com.example.foodmenu.DataBaseHandler;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;

public interface Ihandler {
    void Bind_Data(String id, RecyclerView recyclerView, Context contex);
}
