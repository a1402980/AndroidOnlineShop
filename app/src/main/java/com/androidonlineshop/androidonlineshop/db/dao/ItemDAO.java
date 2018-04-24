package com.androidonlineshop.androidonlineshop.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.util.List;

/**
 * Created by ibraa on 17-Apr-18.
 */

@Dao
public interface ItemDAO {

    @Query("SELECT * FROM item")
    List<ItemEntity> getAllItems();

    @Query("SELECT * FROM item where id = :id")
    ItemEntity findById(Long id);

    @Query("SELECT * FROM item where name = :name")
    ItemEntity findByName(String name);

    @Query("SELECT COUNT(*) from Item")
    int countItems();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<ItemEntity> items);
    @Insert
    long insertItem(ItemEntity item);

    @Update
    void update(ItemEntity item);

    @Delete
    void delete(ItemEntity item);
}