package com.androidonlineshop.androidonlineshop.db.async.category;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class GetCategory extends AsyncTask<Long, Void, CategoryEntity> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public GetCategory(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected CategoryEntity doInBackground(Long... longs) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        return dbCreator.getDatabase().categoryDAO().findById(longs[0]);
    }
}
