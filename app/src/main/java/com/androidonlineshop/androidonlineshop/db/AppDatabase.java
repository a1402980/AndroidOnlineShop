package com.androidonlineshop.androidonlineshop.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.androidonlineshop.androidonlineshop.db.dao.CartDAO;
import com.androidonlineshop.androidonlineshop.db.dao.CategoryDAO;
import com.androidonlineshop.androidonlineshop.db.dao.ItemDAO;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

@Database(version = 1, entities = {ItemEntity.class, CategoryEntity.class, CartEntity.class})
public abstract class AppDatabase extends RoomDatabase {

    static final String DATABASE_NAME = "onlineshop-db";

    public abstract ItemDAO itemDAO();

    public abstract CategoryDAO categoryDAO();

    public abstract CartDAO cartDAO();

}
