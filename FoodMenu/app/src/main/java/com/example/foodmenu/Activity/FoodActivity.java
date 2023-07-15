package com.example.foodmenu.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Factory.FoodFactory;
import com.example.foodmenu.Fragments.AddFoodFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

public class FoodActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ManageFragment();
    }

    private void ManageFragment(){
        FragmentUtils.ReplaceFragment(getSupportFragmentManager(), R.id.foodActivity_FrameLayout,
                new AddFoodFragment());
    }
}