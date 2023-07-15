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

import com.bumptech.glide.Glide;
import com.example.foodmenu.Activity.MainActivity;
import com.example.foodmenu.DataBaseHandler.FoodHandler;
import com.example.foodmenu.Entity.Item;
import com.example.foodmenu.R;
import com.example.foodmenu.Utils.ValidatorUtils;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;

public class EditFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final int PICK_IMAGE_REQUEST = 1;

    private String mParam1;
    private String mParam2;

    private TextInputEditText name_EditText, price_EditText;
    private Button update_Button, pick_image_Button;
    private ImageView image_ImageView;

    private Uri image_Uri;

    private Item item;

    public EditFragment(Item _item) {
        this.item = _item;
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
        return inflater.inflate(R.layout.fragment_edit, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InitViews(view);
        BindData();
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
        update_Button.setOnClickListener(v -> {
            //TODO
        });

        pick_image_Button.setOnClickListener(v -> {
            openImagePicker();
        });
    }

    private void BindData(){
        name_EditText.setText(item.getName());
        price_EditText.setText(item.getPrice());
        Glide.with(getContext()).load(item.getImageUrl()).into(image_ImageView);
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

        update_Button = view.findViewById(R.id.update_Button);
        pick_image_Button = view.findViewById(R.id.pick_image_Button);

        image_ImageView = view.findViewById(R.id.image_ImageView);
    }
}