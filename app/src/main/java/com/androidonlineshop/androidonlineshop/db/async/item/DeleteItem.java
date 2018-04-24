package com.androidonlineshop.androidonlineshop.db.async.item;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class DeleteItem extends AsyncTask<ItemEntity, Void, Void> {

    private final WeakReference<View> mView;

    public DeleteItem(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(ItemEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        for (ItemEntity item : params)
            dbCreator.getDatabase().itemDAO().delete(item);
        return null;
    }
}
