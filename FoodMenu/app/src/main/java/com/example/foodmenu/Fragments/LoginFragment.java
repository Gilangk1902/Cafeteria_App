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
import android.widget.Toast;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.App_Start.Session;
import com.example.foodmenu.DataBaseHandler.DatabaseSingleton;
import com.example.foodmenu.DataBaseHandler.UserHandler;
import com.example.foodmenu.Entity.User;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class LoginFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView error_message_TextView;
    private Button login_Button;
    private TextInputEditText password_TextInputEditText, email_TextInputEditText;

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

    private void Listeners(){
        login_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ValidatorUtils.isTextInputEmpty(email_TextInputEditText) &&
                        !ValidatorUtils.isTextInputEmpty(password_TextInputEditText)){
                    String email = email_TextInputEditText.getText().toString();
                    String password = password_TextInputEditText.getText().toString();

                    UserHandler userHandler = new UserHandler();

                    if(isAdmin(email)){
                        userHandler.getAdmins().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot adminSnapshot : snapshot.getChildren()){
                                    Login(adminSnapshot, email, password, "admin");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    else{
                        userHandler.getCustomers().addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot customerSnapShot : snapshot.getChildren()){
                                    Login(customerSnapShot, email, password, "customer");
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }
        });
    }

    private void Login(DataSnapshot customerSnapShot, String email, String password, String role){
        if(customerSnapShot.child("email")
                .getValue(String.class).equals(email)){
            if(customerSnapShot.child("password")
                    .getValue(String.class).equals(password)){
                String _email, _password, _name;
                _name = customerSnapShot.child("name").getValue(String.class);
                _email = customerSnapShot.child("email").getValue(String.class);
                _password = customerSnapShot.child("password").getValue(String.class);

                Session.Login(
                        new User(_name, _email, _password),
                        role
                );

                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            else{
                //Password Doesn't Match
            }
        }
    }

    private void InitViews(View view){
        error_message_TextView = view.findViewById(R.id.error_message_TextView);
        password_TextInputEditText = view.findViewById(R.id.password_TextInputEditText);
        email_TextInputEditText = view.findViewById(R.id.email_TextInputEditText);
        login_Button = view.findViewById(R.id.login_Button);
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