package com.androidonlineshop.androidonlineshop.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class ItemEntity implements Serializable{

    @NonNull
    private String uid;

    private String name;
    private double price;
    private String description;
    private int rating;
    private boolean isSold;

    private String cartid;
    private String categoryid;


    public ItemEntity() {}

    public ItemEntity(String uid, String name, double price, String description, int rating, String cartid, String categoryid, boolean isSold)
    {
        this.uid = uid;
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
        this.cartid = cartid;
        this.categoryid = categoryid;
        this.isSold = isSold;
    }

    @Exclude
    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }

    public void setPrice(double price) { this.price = price; }

    public String getDescription() { return description; }

    public void setDescription(String description) { this.description = description; }

    public int getRating() { return rating; }

    public void setRating(int rating) { this.rating = rating; }

    public String getCartid() { return cartid; }

    public void setCartid(String cartid) { this.cartid = cartid; }

    public String getCategoryid() { return categoryid; }

    public void setCategoryid(String categoryid) { this.categoryid = categoryid; }

    public boolean isSold() { return isSold; }

    public void setSold(boolean isSold) { this.isSold = isSold; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("price", price);
        result.put("description", description);
        result.put("rating", rating);
        result.put("sold", isSold);
        result.put("cartid", cartid);
        result.put("categoryid", categoryid);

        return result;
    }
}