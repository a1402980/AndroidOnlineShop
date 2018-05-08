package com.androidonlineshop.androidonlineshop.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class CartEntity implements Serializable{

    @NonNull
    private String uid;

    private int quantity;

    public CartEntity() {}

    public CartEntity(int quantity) {
        this.quantity = quantity;
    }

    @Exclude
    public String getUid() { return uid; }

    public void setUid(String uid) { this.uid = uid; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", quantity);
        return result;
    }

}
