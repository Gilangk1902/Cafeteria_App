package com.example.foodmenu.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.Food;
import com.example.foodmenu.Factory.FoodFactory;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddFoodFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mParam1;
    private String mParam2;

    private TextInputEditText name_EditText, price_EditText;
    private Button add_Button, pick_image_Button;
    private ImageView image_ImageView;

    private Uri image_Uri;

    public AddFoodFragment() {
        // Required empty public constructor
    }

    public static AddFoodFragment newInstance(String param1, String param2) {
        AddFoodFragment fragment = new AddFoodFragment();
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
        return inflater.inflate(R.layout.fragment_add_food, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        Listeners();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && data!=null){
            Uri selectedImageUri = data.getData();
            setImageView_Image(selectedImageUri);
            image_Uri =  selectedImageUri;
        }
    }

    private void Listeners(){
        add_Button.setOnClickListener(v -> {
            if(!ValidatorUtils.isTextInputEmpty(name_EditText) &&
                    !ValidatorUtils.isTextInputEmpty(price_EditText))
            {
                AddItem();
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(getActivity(), "masih salah brok", Toast.LENGTH_SHORT).show();
            }
        });

        pick_image_Button.setOnClickListener(v -> {
            openImagePicker();
        });
    }

    private void AddItem(){
        FoodHandler foodHandler = new FoodHandler();

        if(image_Uri!=null){
            StorageReference imageRef = foodHandler.getFoodStorageReference()
                    .child(name_EditText.getText().toString()+".jpg");

            UploadTask uploadTask = imageRef.putFile(image_Uri);
            uploadTask.addOnSuccessListener(taskSnapshot ->
            {
               imageRef.getDownloadUrl().addOnSuccessListener(uri ->
               {
                   String image_Url = uri.toString();

                   Food food = FoodFactory.Create(name_EditText.getText().toString(),
                           price_EditText.getText().toString(),
                           image_Url);

                   DatabaseReference newFood = foodHandler.getFoodDatabaseReference().child(food.getId());
                   newFood.child("name").setValue(food.getName());
                   newFood.child("price").setValue(food.getPrice());
                   newFood.child("image").setValue(food.getImageUrl());
               });
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "fail", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void setImageView_Image(Uri selectedImageUri) {
        image_ImageView.setImageURI(selectedImageUri);
    }

    private void openImagePicker(){
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private void InitViews(View view){
        name_EditText = view.findViewById(R.id.name_TextInputEditText);
        price_EditText =  view.findViewById(R.id.price_TextInputEditText);
        add_Button = view.findViewById(R.id.add_Button);
        pick_image_Button = view.findViewById(R.id.pick_image_Button);
        image_ImageView = view.findViewById(R.id.image_ImageView);
    }
}