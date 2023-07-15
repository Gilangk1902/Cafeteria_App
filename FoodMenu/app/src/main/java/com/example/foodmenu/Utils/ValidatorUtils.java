package com.example.foodmenu.Utils;

import android.widget.TextView;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class ValidatorUtils {
    public static Boolean isEmail(TextInputEditText email){
        if(email.getText().toString().endsWith("@gmail.com")){
            return true;
        }
        else{
            return false;
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

    public static Boolean isSomethingEmpty(ArrayList<TextInputEditText> editTexts){
        for(TextInputEditText editText : editTexts){
            if(isTextInputEmpty(editText)){
                return true;
            }
        }
        return false;
    }

    public static Boolean isTextInputEquals(TextInputEditText textInputA,
                                            TextInputEditText textInputB){
        if(textInputA.getText().toString().equals(textInputB.getText().toString())){
            return true;
        }
        else{
            return false;
        }
    }
}
