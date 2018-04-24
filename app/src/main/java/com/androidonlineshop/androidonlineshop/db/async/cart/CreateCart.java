package com.androidonlineshop.androidonlineshop.db.async.cart;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class CreateCart extends AsyncTask<CartEntity, Void, Boolean> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public CreateCart(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Boolean doInBackground(CartEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        boolean response = true;
        try {
            for (CartEntity cart : params)
                dbCreator.getDatabase().cartDAO().insertCart(cart);

        } catch (SQLiteConstraintException e) {
            response = false;
        }
        return response;
    }
    
}
