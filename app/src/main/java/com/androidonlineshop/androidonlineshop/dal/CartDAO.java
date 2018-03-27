package com.androidonlineshop.androidonlineshop.dal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.androidonlineshop.androidonlineshop.model.Cart;
import com.androidonlineshop.androidonlineshop.model.Item;

import java.util.List;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Dao
public interface CartDAO {

    @Query("SELECT * FROM cart")
    List<Cart> getAllItems();

    @Query("SELECT COUNT(*) from cart")
    int countCartItems();

    @Insert
    void insertIntoCart(Item item);

    @Delete
    void delete(Item item);
}
