package com.androidonlineshop.androidonlineshop.model;

import java.io.Serializable;

/**
 * Created by ibraa on 27-Mar-18.
 */

public class Item{

    private String name;
    private double price;
    private String description;
    private int rating;

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
