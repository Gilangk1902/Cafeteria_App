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
import com.example.foodmenu.Entity.Admin;
import com.example.foodmenu.Entity.Customer;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.FragmentUtils;

public class ProfileFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView name, email;
    private Button logout_Button, cart_order_Button;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        InitViews(view);
        Listeners();
        BindData();
    }

    private void InitViews(View view){
        name =  view.findViewById(R.id.user_name_TextView);
        email = view.findViewById(R.id.user_email_TextView);
        logout_Button = view.findViewById(R.id.logout_Button);
        cart_order_Button = view.findViewById(R.id.cart_Button);
    }

    private void Listeners(){
        logout_Button.setOnClickListener(v -> {
            Session.Logout();
            Intent intent = new Intent(getContext(), MainActivity.class);
            startActivity(intent);
        });
        cart_order_Button.setOnClickListener(v->{
            if(Session.getUser().getId().contains(Customer.CODE)){
                FragmentUtils.ReplaceFragment(
                        getParentFragmentManager(), R.id.user_FrameLayout, new CartFragment()
                );
            }
            else if(Session.getUser().getId().contains(Admin.CODE)){
                FragmentUtils.ReplaceFragment(
                        getParentFragmentManager(), R.id.user_FrameLayout, new OrdersFragment()
                );
            }
        });
    }

    private void BindData(){
        name.setText("Name : " + Session.getUser().getName() + "#" + Session.getUser().getId());
        email.setText("Email : " + Session.getUser().getEmail());
        if(Session.getUser().getId().contains(Customer.CODE)){
            cart_order_Button.setText("Your Cart");
        }
        else if(Session.getUser().getId().contains(Admin.CODE)){
            cart_order_Button.setText("Orders");
        }
    }
}