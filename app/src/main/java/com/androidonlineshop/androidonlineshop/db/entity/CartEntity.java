package com.androidonlineshop.androidonlineshop.db.entity;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by ibraa on 17-Apr-18.
 */

@Entity(tableName = "cart")
public class CartEntity implements Serializable{

    @PrimaryKey(autoGenerate = true)
    private long id;

    private int quantity;

    public CartEntity() {}

    public CartEntity(int quantity) {
        this.quantity = quantity;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public int getQuantity() { return quantity; }

    public void setQuantity(int quantity) { this.quantity = quantity; }

}
