package com.example.foodmenu.Factory;

import com.example.foodmenu.Entity.Food;

import java.util.Random;

public class FoodFactory {
    public static Food Create(String name, String price, String URL){
        return new Food(GenerateID(),name, price, URL);
    }
    private static String GenerateID(){
        String itemCode = "FD";

        Random random  = new Random();
        int randomNumber = random.nextInt(900) + 100;
        String randomNumber_inString = String.valueOf(randomNumber);

        return itemCode + randomNumber_inString;
    }
}
