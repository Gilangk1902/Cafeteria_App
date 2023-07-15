package com.example.foodmenu.Utils;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

public class ValidatorUtils {
    public static Boolean isEmail(String email, TextView error_message){
        if(!email.contains("@gmail.com")){
            error_message.setText("email is not valid");
            return false;
        }
        else{
            return true;
        }
    }

    public static Boolean isTextInputEmpty(TextInputEditText textInput){
        if(textInput==null){
            return true;
        }
        else if(textInput.getText().toString().equals("")){
            return true;
        }
        else{
            return false;
        }
    }
}
