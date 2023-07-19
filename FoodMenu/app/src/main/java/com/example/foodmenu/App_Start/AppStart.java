package com.example.foodmenu.App_Start;

import android.app.Application;

import com.google.firebase.FirebaseApp;

public class AppStart extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        Session.InitUser();
    }
}
