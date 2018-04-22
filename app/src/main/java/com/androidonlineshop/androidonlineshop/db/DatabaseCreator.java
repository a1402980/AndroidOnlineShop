package com.androidonlineshop.androidonlineshop.db;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CategoryWithItems;

import java.util.ArrayList;
import java.util.List;

import static com.androidonlineshop.androidonlineshop.db.AppDatabase.DATABASE_NAME;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class DatabaseCreator {

    public static final String TAG = "DatabaseCreator";

    private static DatabaseCreator sInstance;

    private AppDatabase mDb;

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    public synchronized static DatabaseCreator getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new DatabaseCreator();
                }
            }
        }
        return sInstance;
    }

    @Nullable
    public AppDatabase getDatabase() {
        return mDb;
    }

    /**
     * Creates or returns a previously-created database.
     * <p>
     * Although this uses an AsyncTask which currently uses a serial executor, it's thread-safe.
     */
    public void createDb(Context context) {

        Log.d("DatabaseCreator", "Creating DB from " + Thread.currentThread().getName());


        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... params) {
                Log.d(TAG, "Starting bg job " + Thread.currentThread().getName());

                Context context = params[0].getApplicationContext();

                // Reset the database to have new data on every run.
                context.deleteDatabase(DATABASE_NAME);

                // Build the database!
                AppDatabase db = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class, DATABASE_NAME).build();

                // Add a delay to simulate a long-running operation
                addDelay();

                // Add some data to the database
                generateData(db);
                List<CategoryEntity> categories = db.categoryDAO().getAllCategories();
                for(CategoryEntity category : categories) {
                    Log.d(TAG, category.getName());
                    System.out.println(category.getId());
                }
                List<ItemEntity> items = db.itemDAO().getAllItems();
                for(ItemEntity item : items) {
                    Log.d(TAG, item.getName());
                    System.out.println(item.getCategoryid());
                }

                List<CategoryWithItems> categoryWithItems = db.categoryDAO().loadCategoriesWithItems();
                for(CategoryWithItems categoryWithItems1 : categoryWithItems)
                {
                    System.out.println(categoryWithItems1.items.size());
                }

                Log.d(TAG, "DB was populated in thread " + Thread.currentThread().getName());

                mDb = db;
                return null;
            }
        }.execute(context.getApplicationContext());
    }

    private void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    private void generateData(AppDatabase db){

        List<ItemEntity> items = new ArrayList<>();
        List<CategoryEntity> categories = new ArrayList<>();

        CategoryEntity accessories = new CategoryEntity("Accessories", "In this category belong accessories!");
        CategoryEntity laptops = new CategoryEntity("Laptop", "In this category belong laptops!");

        categories.add(accessories);
        categories.add(laptops);

        CartEntity cart = new CartEntity();

        ItemEntity lenovo = new ItemEntity("Lenovo Laptop", 565.00, "New laptop.", 5, cart.getId(), laptops.getId());
        ItemEntity hp = new ItemEntity("HP Laptop", 450.00, "New laptop.", 4, cart.getId(), laptops.getId());
        ItemEntity headphones = new ItemEntity("JBL HeadPhones", 45.00, "New headphones.", 5, cart.getId(), accessories.getId());
        ItemEntity phonecase = new ItemEntity("Iphone 7 case", 15.00, "Phone Case for iphone 7", 3, cart.getId(), accessories.getId());

        items.add(lenovo);
        items.add(hp);
        items.add(headphones);
        items.add(phonecase);

        cart.setQuantity(items.size());

        db.categoryDAO().insertAll(categories);
        db.cartDAO().insertCart(cart);
        db.itemDAO().insertAll(items);
    }
}