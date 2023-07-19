package com.example.foodmenu.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.foodmenu.DataBaseHandler.CustomerHandler;
import com.example.foodmenu.Entity.User;
import com.example.foodmenu.Factory.UserFactory;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RegisterFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextInputEditText email_TextInputEditText, name_TextInputEditText,
                            password_TextInputEditText, confirm_password_TextInputEditText;
    private ArrayList<TextInputEditText> editTexts = new ArrayList<>();
    private Button register_Button, cancel_Button;
    private TextView error_message_TextView, login_TextButton;

    public RegisterFragment() {
        // Required empty public constructor
    }

    public static RegisterFragment newInstance(String param1, String param2) {
        RegisterFragment fragment = new RegisterFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        Listeners();
    }

    private void Listeners(){
        register_Button.setOnClickListener(v -> {
            if(isRegisterValid()){
                Register();
            }
        });

        login_TextButton.setOnClickListener(v -> {
            FragmentUtils.ReplaceFragment(getParentFragmentManager(), R.id.user_FrameLayout,
                                            new LoginFragment());
        });
    }

    private void Register(){
        User newUser = UserFactory.Create(
                email_TextInputEditText.getText().toString(),
                name_TextInputEditText.getText().toString(),
                password_TextInputEditText.getText().toString()
        );

        CustomerHandler customerHandler = new CustomerHandler();
        customerHandler.Register(newUser);

        FragmentUtils.ReplaceFragment(getParentFragmentManager(),
                R.id.user_FrameLayout, new LoginFragment());
    }

    private boolean isRegisterValid(){
        if(!ValidatorUtils.isSomethingEmpty(editTexts)){
            if(ValidatorUtils.isEmail(email_TextInputEditText)){
                if(ValidatorUtils.isTextInputEquals(password_TextInputEditText,
                        confirm_password_TextInputEditText)){
                    return true;
                }
                else{
                    error_message_TextView.setText("Password doesn't match");
                    return false;
                }
            }
            else{
                error_message_TextView.setText("email doesnt end with @gmail.com");
                return false;
            }
        }
        else{
            error_message_TextView.setText("You need to fill all the required things dumb ass");
            return false;
        }
    }

    private void InitViews(View view){
        email_TextInputEditText = view.findViewById(R.id.email_TextInputEditText);
        editTexts.add(email_TextInputEditText);
        name_TextInputEditText = view.findViewById(R.id.name_TextInputEditText);
        editTexts.add(name_TextInputEditText);
        password_TextInputEditText = view.findViewById(R.id.password_TextInputEditText);
        editTexts.add(password_TextInputEditText);
        confirm_password_TextInputEditText = view.findViewById(R
                                            .id.confirm_password_TextInputEditText);
        editTexts.add(confirm_password_TextInputEditText);

        register_Button = view.findViewById(R.id.register_Button);
        cancel_Button = view.findViewById(R.id.cancel_Button);
        login_TextButton = view.findViewById(R.id.login_TextButton);

        error_message_TextView = view.findViewById(R.id.error_message_TextView);

    }


}