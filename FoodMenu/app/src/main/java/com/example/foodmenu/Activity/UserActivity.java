package com.example.foodmenu.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.Fragments.LoginFragment;
import com.example.foodmenu.Fragments.ProfileFragment;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

public class UserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        if(Session.getUser().getName()==null){
            ChangeFrameLayout(new LoginFragment());
        }
        else{
            ChangeFrameLayout(new ProfileFragment());
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(UserActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void ChangeFrameLayout(Fragment fragment){
        FragmentUtils.ReplaceFragment(getSupportFragmentManager(),
                R.id.user_FrameLayout, fragment);
    }
}