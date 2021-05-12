package com.example.a3dmarket;

import android.graphics.drawable.Drawable;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Item {


    //here i can use String variable to store image URL

    private String img;
    private String name;
    private String price;

    public Item(String img, String name, String price) {
        this.img = img;
        this.name = name;
        this.price = price;
    }

    public Item(String img) {
        this.img = img;
    }

    public void setImg(String img) {
        this.img = img;
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



    public String getImg() {
        return img;
    }
}
