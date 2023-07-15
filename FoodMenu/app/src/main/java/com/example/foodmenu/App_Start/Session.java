package com.example.foodmenu.App_Start;

import com.example.foodmenu.Entity.User;

public class Session {
    private static User user;
    private static String role;

    public static void InitUser(){
        user = new User(null,null,null);
    }

    public static void Login(User newUser, String _role){
        user.setName(newUser.getName());
        user.setEmail(newUser.getEmail());
        user.setPassword(newUser.getPassword());
        role = _role;
    }

    public static void Logout(){
        user = new User(null, null, null);
        role = "";
    }

    public static User getUser(){
        return user;
    }

    public static String getRole(){
        return role;
    }
}
