package com.androidonlineshop.androidonlineshop.room;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.androidonlineshop.androidonlineshop.dal.CartDAO;
import com.androidonlineshop.androidonlineshop.dal.CategoryDAO;
import com.androidonlineshop.androidonlineshop.dal.ItemDAO;
import com.androidonlineshop.androidonlineshop.model.Cart;
import com.androidonlineshop.androidonlineshop.model.Category;
import com.androidonlineshop.androidonlineshop.model.Item;

/**
 * Created by Ibrahim Beqiri on 27-Mar-18.
 */

@android.arch.persistence.room.Database(version = 1, entities = {Item.class, Category.class, Cart.class})
public abstract class Database extends RoomDatabase{

    public abstract ItemDAO itemDAO();

    public abstract CategoryDAO categoryDAO();

    public abstract CartDAO cartDAO();


    private static Database INSTANCE;


    /*public static Database getDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), Database.class, "database")
                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.
                            //.allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }*/
}
