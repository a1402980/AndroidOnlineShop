package com.androidonlineshop.androidonlineshop.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by ibraa on 17-Apr-18.
 */

@Entity(tableName = "category")
public class CategoryEntity{

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String name;
    private String description;

    public CategoryEntity() {}

    public CategoryEntity(String name, String description)
    {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
