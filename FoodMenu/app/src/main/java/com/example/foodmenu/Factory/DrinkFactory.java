package com.example.foodmenu.Factory;

import com.example.foodmenu.Entity.Drink;
import com.example.foodmenu.Entity.Food;

import java.util.Random;

public class DrinkFactory {
    public static Drink Create(String name, String price, String URL){
        return new Drink(GenerateID(),name, price, URL);
    }
    private static String GenerateID(){
        String itemCode = "DK";

        Random random  = new Random();
        int randomNumber = random.nextInt(900) + 100;
        String randomNumber_inString = String.valueOf(randomNumber);

        return itemCode + randomNumber_inString;
    }
}
