package com.androidonlineshop.androidonlineshop.dal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.androidonlineshop.androidonlineshop.model.Item;

import java.util.List;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM item")
    List<Item> getAllItems();

    @Query("SELECT * FROM item where name LIKE :name")
    Item findByName(String name);

    @Query("SELECT COUNT(*) from item")
    int countItems();

    @Insert
    void insertItem(Item item);

    @Delete
    void delete(Item item);
}
