package com.androidonlineshop.androidonlineshop.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;
import android.database.sqlite.SQLiteConstraintException;

import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CategoryWithItems;

import java.util.List;

/**
 * Created by ibraa on 17-Apr-18.
 */

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM category")
    List<CategoryEntity> getAllCategories();

    @Query("SELECT * FROM category where name = :name")
    CategoryEntity findByName(String name);

    @Query("SELECT * FROM category where id = :id")
    CategoryEntity findById(String id);

    @Query("SELECT * FROM category")
    List<CategoryWithItems> loadCategoriesWithItems();

    @Query("SELECT COUNT(*) from category")
    int countCategories();

    @Insert
    void insertCategory(CategoryEntity category) throws SQLiteConstraintException;

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CategoryEntity> categories);

    @Update
    void update(CategoryEntity category);

    @Delete
    void delete(CategoryEntity category);
}