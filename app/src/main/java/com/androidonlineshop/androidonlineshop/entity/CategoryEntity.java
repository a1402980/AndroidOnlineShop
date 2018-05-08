package com.androidonlineshop.androidonlineshop.entity;

import android.support.annotation.NonNull;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class CategoryEntity implements Serializable{

    @NonNull
    private String  uid;

    private String name;
    private String description;

    public CategoryEntity() {}

    public CategoryEntity(String uid, String name, String description)
    {
        this.uid = uid;
        this.name = name;
        this.description = description;
    }

    @Exclude
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("description", description);

        return result;
    }

}
