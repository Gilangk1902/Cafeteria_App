package com.example.foodmenu.Entity;

import java.io.Serializable;

public class Item implements Serializable {
    private String id;
    private String name;
    private String price;
    private String imageUrl;

    public Item(String id,String name, String price, String imageUrl){
        this.name  = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
