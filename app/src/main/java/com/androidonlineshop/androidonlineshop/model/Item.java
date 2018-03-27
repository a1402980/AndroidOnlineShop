package com.androidonlineshop.androidonlineshop.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Entity
public class Item{

    @PrimaryKey
    private int itemid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "price")
    private double price;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "rating")
    private int rating;

    @ColumnInfo(name = "category")
    private Category category;

    public Item() {}

    public Item(String name, double price, String description, int rating)
    {
        this.name = name;
        this.price = price;
        this.description = description;
        this.rating = rating;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

}
