package com.androidonlineshop.androidonlineshop.db.async.category;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class GetCategory extends AsyncTask<String, Void, CategoryEntity> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetCategory(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected CategoryEntity doInBackground(String... strings) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().categoryDAO().findById(strings[0]);
    }
}
