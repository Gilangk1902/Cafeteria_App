package com.example.foodmenu.Activity;

import static com.example.foodmenu.Utils.FragmentUtils.ReplaceFragment;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Fragments.*;
import com.example.foodmenu.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainActivity_FrameLayout;
    private TextView title, welcome_sign;
    private DatabaseReference testRef;
    private BottomNavigationView bottomNavigationView;
    private FloatingActionButton floatingActionButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        InitViews();
        ChangeToShowAll();
        BottomNav_Listeners();
        setMainActivityItems();
        Listeners();
    }

    private void BottomNav_Listeners(){
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {
            if(item.getItemId() == R.id.home_nav_item){
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent);
                return true;
            }
            else if(item.getItemId() == R.id.user_nav_item){
                Intent intent = new Intent(MainActivity.this, UserActivity.class);
                intent.putExtra(UserActivity.PROFILE_KEY, 1);
                startActivity(intent);
                return true;
            }
            return false;
        });
    }

    private void Listeners(){
        floatingActionButton.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FoodActivity.class);
            intent.putExtra(FoodActivity.ADD_KEY, "");
            startActivity(intent);
        });
    }

    private void ChangeToShowAll(){
        ReplaceFragment(getSupportFragmentManager(), R.id.mainActivity_FrameLayout,
                new ShowAllFragment());
    }

    private void InitViews(){
        welcome_sign =  findViewById(R.id.welcome_sign_TextView);
        mainActivity_FrameLayout = findViewById(R.id.mainActivity_FrameLayout);
        bottomNavigationView  = findViewById(R.id.app_navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.home_nav_item);
        floatingActionButton  = findViewById(R.id.floatingButton);
    }

    private void setMainActivityItems(){
        if(Session.getUser().getEmail()!=null){
            if(Session.getRole().equals("customer")){
                welcome_sign.setText("Welcome, "  + Session.getUser().getName());
                floatingActionButton.hide();
            }
            else if(Session.getRole().equals("admin")){
                welcome_sign.setText("Welcome, bitch ass ni-");
                floatingActionButton.show();
            }
        }
        else{
            welcome_sign.setText("Welcome, Guest");
            floatingActionButton.hide();
        }
    }
}