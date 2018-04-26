package com.androidonlineshop.androidonlineshop.db.async.cart;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;

import java.lang.ref.WeakReference;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class UpdateCart extends AsyncTask<CartEntity, Void, Void> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public UpdateCart(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(CartEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());

        for (CartEntity cart : params) {
            dbCreator.getDatabase().cartDAO().update(cart);
        }
        return null;
    }
}
