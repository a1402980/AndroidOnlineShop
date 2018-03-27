package com.androidonlineshop.androidonlineshop.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Entity
public class Cart {

    @PrimaryKey(autoGenerate = true)
    private int cartid;

    @ColumnInfo(name = "quantity")
    private int quantity;

    /*@ColumnInfo(name = "item")
    private Item item;*/

    public Cart() {
    }

    public Cart(int quantity) {
        this.quantity = quantity;
    }

    public int getCartid() {
        return cartid;
    }

    public void setCartid(int cartid) {
        this.cartid = cartid;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /*public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }*/
}