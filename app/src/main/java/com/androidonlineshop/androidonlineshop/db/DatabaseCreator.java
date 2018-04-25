package com.androidonlineshop.androidonlineshop.db;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
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
import java.util.concurrent.atomic.AtomicBoolean;

import static com.androidonlineshop.androidonlineshop.db.AppDatabase.DATABASE_NAME;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class DatabaseCreator {

    public static final String TAG = "DatabaseCreator";

    private static DatabaseCreator sInstance;

    private AppDatabase mDb;

    private final AtomicBoolean mInitializing = new AtomicBoolean(true);

    private final MutableLiveData<Boolean> mIsDatabaseCreated = new MutableLiveData<>();

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
    public LiveData<Boolean> isDatabaseCreated() {
        return mIsDatabaseCreated;
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

        if (!mInitializing.compareAndSet(true, false)) {
            return; // Already initializing
        }

        mIsDatabaseCreated.setValue(false);// Trigger an update to show a loading screen.
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

                Log.d(TAG, "DB was populated in thread " + Thread.currentThread().getName());

                mDb = db;
                return null;
            }

            @Override
            protected void onPostExecute(Void ignored) {
                // Now on the main thread, notify observers that the db is created and ready.
                mIsDatabaseCreated.setValue(true);
            }
        }.execute(context.getApplicationContext());
    }

    private void addDelay() {
        try {
            Thread.sleep(4000);
        } catch (InterruptedException ignored) {
        }
    }

    public void generateData(AppDatabase db){

        List<ItemEntity> items = new ArrayList<>();
        List<CategoryEntity> categories = new ArrayList<>();

        CategoryEntity accessories = new CategoryEntity(1L,"Accessories", "In this category belong accessories!");
        CategoryEntity laptops = new CategoryEntity(2L, "Laptop", "In this category belong laptops!");
        CategoryEntity TVs = new CategoryEntity(3L, "TVs", "In this category belong TVs!");
        CategoryEntity phones = new CategoryEntity(4L, "Phones", "In this category belong phones");
        CategoryEntity printers = new CategoryEntity(5L, "Printers", "In this category belong printers");

        categories.add(accessories);
        categories.add(laptops);
        categories.add(TVs);
        categories.add(phones);
        categories.add(printers);

        CartEntity cart = new CartEntity();
        cart.setId(1L);
        cart.setQuantity(0);

        ItemEntity lenovo = new ItemEntity("Lenovo Laptop", 565.00, "New laptop.", 5, 0, laptops.getId(), false);
        ItemEntity hp = new ItemEntity("HP Laptop", 450.00, "New laptop.", 4, 0, laptops.getId(), false);
        ItemEntity headphones = new ItemEntity("JBL HeadPhones", 45.00, "New headphones.", 5, 0, accessories.getId(), false);
        ItemEntity phonecase = new ItemEntity("iphone 7 case", 15.00, "Phone Case for iphone 7", 3, 0, accessories.getId(), false);
        ItemEntity panasonicTV = new ItemEntity("Panasonic 55' 4k TV",899.00,"This TV is in great condition. I bought it one year ago",5,0,TVs.getId(), false);
        ItemEntity iphone = new ItemEntity("iphone 5s",399.00,"The phone is cracked a little bit but works just fine",2,cart.getId(),phones.getId(), true);
        ItemEntity brotherprinter = new ItemEntity("Brother printer",49.00,"Used printer for 2 years and now I have a new one. Works just fine. Has some colors included.",4,cart.getId(),printers.getId(), true);

        items.add(lenovo);
        items.add(hp);
        items.add(headphones);
        items.add(phonecase);
        items.add(panasonicTV);
        items.add(iphone);
        items.add(brotherprinter);


        db.categoryDAO().insertAll(categories);
        db.cartDAO().insertCart(cart);
        db.itemDAO().insertAll(items);
    }
}
