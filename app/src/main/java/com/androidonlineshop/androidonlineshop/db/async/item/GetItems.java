package com.androidonlineshop.androidonlineshop.db.async.item;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class GetItems extends AsyncTask<Void, Void, List<ItemEntity>> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetItems(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected List<ItemEntity> doInBackground(Void... voids) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().itemDAO().getAllItems();
    }
}
