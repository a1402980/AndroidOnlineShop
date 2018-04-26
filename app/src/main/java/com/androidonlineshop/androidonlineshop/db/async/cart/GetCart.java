package com.androidonlineshop.androidonlineshop.db.async.cart;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ibrahim Beqiri on 24-Apr-18.
 */

public class GetCart extends AsyncTask<Void, Void, CartEntity> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetCart(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected CartEntity doInBackground(Void... voids) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().cartDAO().getCart();
    }
}
