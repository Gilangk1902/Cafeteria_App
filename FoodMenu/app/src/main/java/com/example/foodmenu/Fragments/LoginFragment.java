package com.example.foodmenu.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.UserHandler;
import com.example.foodmenu.Entity.User;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class LoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView error_message_TextView, register_TextButton;
    private Button login_Button;
    private TextInputEditText password_TextInputEditText, email_TextInputEditText;
    private ArrayList<TextInputEditText> editTexts = new ArrayList<>();

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        Listeners();
    }

    private void Login_onClick(){
        if(!ValidatorUtils.isSomethingEmpty(editTexts)){
            String email = email_TextInputEditText.getText().toString();
            String password = password_TextInputEditText.getText().toString();

            UserHandler userHandler = new UserHandler();

            if(isAdmin(email)){
                userHandler.getAdmins().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot adminSnapshot : snapshot.getChildren()){
                            userHandler.Login(adminSnapshot, email, password, "admin", getContext());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            else{
                userHandler.getCustomersDatabaseReference().addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot customerSnapShot : snapshot.getChildren()){
                            userHandler.Login(customerSnapShot, email, password, "customer", getContext());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        }
    }

    private void Listeners(){
        login_Button.setOnClickListener(v -> {
            Login_onClick();
        });

        register_TextButton.setOnClickListener(v -> {
            FragmentUtils.ReplaceFragment(getParentFragmentManager(), R.id.user_FrameLayout,
                    new RegisterFragment());
        });
    }

    private void InitViews(View view){
        error_message_TextView = view.findViewById(R.id.error_message_TextView);

        password_TextInputEditText = view.findViewById(R.id.password_TextInputEditText);
        editTexts.add(password_TextInputEditText);
        email_TextInputEditText = view.findViewById(R.id.email_TextInputEditText);
        editTexts.add(email_TextInputEditText);

        login_Button = view.findViewById(R.id.login_Button);
        register_TextButton = view.findViewById(R.id.register_TextButton);
    }

    private boolean isAdmin(String str){
        if(str.contains("admin")){
            return  true;
        }
        else{
            return false;
        }
    }
}