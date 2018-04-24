package com.androidonlineshop.androidonlineshop.db.async.category;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class UpdateCategory extends AsyncTask<CategoryEntity, Void, Void> {

    // Weak references will still allow the Activity to be garbage-collected
    private final WeakReference<View> mView;

    public UpdateCategory(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(CategoryEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        for (CategoryEntity category : params)
            dbCreator.getDatabase().categoryDAO().update(category);
        return null;
    }
}
