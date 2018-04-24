package com.androidonlineshop.androidonlineshop.db.async.item;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class GetItem extends AsyncTask<String, Void, ItemEntity> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetItem(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected ItemEntity doInBackground(String ... strings) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().itemDAO().findById(strings[0]);
    }
}
