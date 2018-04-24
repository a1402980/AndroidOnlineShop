package com.androidonlineshop.androidonlineshop.db.async.item;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class CreateItem extends AsyncTask<ItemEntity, Void, Boolean> {

    private final WeakReference<View> mView;

    public CreateItem(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Boolean doInBackground(ItemEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        boolean response = true;
        try {
            for (ItemEntity item : params)
                dbCreator.getDatabase().itemDAO().insertItem(item);

        } catch (SQLiteConstraintException e) {
            response = false;
        }
        return response;
    }
}
