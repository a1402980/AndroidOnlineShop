package com.androidonlineshop.androidonlineshop.db.async.item;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class UpdateItem extends AsyncTask<ItemEntity, Void, Void> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public UpdateItem(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(ItemEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        for (ItemEntity item : params)
            dbCreator.getDatabase().itemDAO().update(item);
        return null;
    }
}
