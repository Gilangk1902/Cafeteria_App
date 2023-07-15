package com.example.foodmenu.Factory;

import com.example.foodmenu.Entity.User;

import java.util.Random;

public class UserFactory {
    public static User Create(String email, String name, String password){
        return new User(GenerateID(), name, email, password);
    }

    private static String GenerateID(){
        String CODE_ID = "CS";
        int number_id;

        Random random = new Random();
        number_id = random.nextInt(900)+100;

        return CODE_ID + number_id;
    }
}
