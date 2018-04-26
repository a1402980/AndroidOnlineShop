package com.androidonlineshop.androidonlineshop.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CartWithItems;

import java.util.List;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

@Dao
public interface CartDAO {

    @Query("SELECT * FROM cart")
    List<CartWithItems> loadCartWithItems();

    @Query("SELECT COUNT(*) from cart")
    int countCartItems();

    @Query("SELECT * FROM cart WHERE id = :id")
    CartEntity getById(String id);

    @Query("SELECT * FROM cart")
    CartEntity getCart();

    @Insert
    void insertAll(List<CartEntity> carts);

    @Insert
    void insertCart(CartEntity cart) throws SQLiteConstraintException;

    @Update
    void update(CartEntity cart);

    @Delete
    void delete(CartEntity cart);

}