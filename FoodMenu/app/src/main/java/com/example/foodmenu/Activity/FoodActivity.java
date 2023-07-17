package com.example.foodmenu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Entity.Item;
import com.example.foodmenu.Fragments.AddFragment;
import com.example.foodmenu.Fragments.DetailsFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

public class FoodActivity extends AppCompatActivity {
    public static final String DETAIL_KEY = "detail";
    public static final String ADD_KEY =  "add";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);
        ManageFragment();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager()
                            .findFragmentById(R.id.foodActivity_FrameLayout);
        if(fragment instanceof AddFragment){
            Intent intent = new Intent(FoodActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(fragment instanceof  DetailsFragment){
            Intent intent = new Intent(FoodActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }

    private void ManageFragment(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras!=null){
            if(extras.containsKey(DETAIL_KEY)){
                FragmentUtils.ReplaceFragment(getSupportFragmentManager(),
                        R.id.foodActivity_FrameLayout,
                        new DetailsFragment((Item) intent.getSerializableExtra(DETAIL_KEY))
                );
            }
            else if(extras.containsKey(ADD_KEY)){
                FragmentUtils.ReplaceFragment(getSupportFragmentManager(),
                        R.id.foodActivity_FrameLayout,
                        new AddFragment()
                );
            }
        }
    }
}