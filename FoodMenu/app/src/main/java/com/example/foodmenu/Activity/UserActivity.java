package com.example.foodmenu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Fragments.CartFragment;
import com.example.foodmenu.Fragments.LoginFragment;
import com.example.foodmenu.Fragments.ProfileFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;
import com.google.firebase.database.core.view.Change;

public class UserActivity extends AppCompatActivity {
    public static final String CART_KEY = "cart_access";
    public static final String PROFILE_KEY = "profile_access";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        getSupportActionBar().hide();

        ManageFragment();
    }

    @Override
    public void onBackPressed() {
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.user_FrameLayout);
        if (fragment instanceof LoginFragment){
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else if(fragment instanceof ProfileFragment){
            Intent intent = new Intent(UserActivity.this, MainActivity.class);
            startActivity(intent);
        }
        else{
            super.onBackPressed();
        }
    }

    public void ManageFragment(){
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras!=null){
            if(extras.containsKey(CART_KEY)){
                ChangeFrameLayout(new CartFragment());
            }
            else if(extras.containsKey(PROFILE_KEY)){
                if(Session.getUser().getName()==null){
                    ChangeFrameLayout(new LoginFragment());
                }
                else{
                    ChangeFrameLayout(new ProfileFragment());
                }
            }
        }
    }

    private void ChangeFrameLayout(Fragment fragment){
        FragmentUtils.ReplaceFragment(getSupportFragmentManager(),
                R.id.user_FrameLayout, fragment);
    }
}