package com.androidonlineshop.androidonlineshop.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Entity
public class Category{

    @PrimaryKey(autoGenerate = true)
    private int categoryid;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    public Category() {}

    public Category(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public int getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(int categoryid) {
        this.categoryid = categoryid;
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


}

