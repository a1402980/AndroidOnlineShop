package com.androidonlineshop.androidonlineshop.db.async.category;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;

/**
 * Created by ibraa on 17-Apr-18.
 */

public class DeleteCategory extends AsyncTask<CategoryEntity, Void, Void> {

    private final WeakReference<View> mView;

    public DeleteCategory(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Void doInBackground(CategoryEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        for (CategoryEntity category : params)
            dbCreator.getDatabase().categoryDAO().delete(category);
        return null;
    }
}
