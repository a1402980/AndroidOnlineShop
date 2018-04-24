package com.androidonlineshop.androidonlineshop.db.async.cart;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.pojo.CartWithItems;
import com.androidonlineshop.androidonlineshop.db.pojo.CategoryWithItems;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ibraa on 24-Apr-18.
 */

public class GetCartWithItems extends AsyncTask<Void, Void, List<CartWithItems>> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetCartWithItems(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected List<CartWithItems> doInBackground(Void... voids) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().cartDAO().loadCartWithItems();
    }
}
