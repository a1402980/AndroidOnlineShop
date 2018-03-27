package com.androidonlineshop.androidonlineshop.dal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.androidonlineshop.androidonlineshop.model.Category;
import com.androidonlineshop.androidonlineshop.model.Item;

import java.util.List;

/**
 * Created by ibraa on 27-Mar-18.
 */

@Dao
public interface CategoryDAO {

    @Query("SELECT * FROM category")
    List<Category> getAllCategories();

    @Query("SELECT * FROM category where name LIKE :name")
    Category findByName(String name);

    @Query("SELECT COUNT(*) from category")
    int countCategories();

    @Insert
    void insertCategory(Category categories);

    @Delete
    void delete(Category category);
}
