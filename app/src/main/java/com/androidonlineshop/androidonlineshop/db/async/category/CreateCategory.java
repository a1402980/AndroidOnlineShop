package com.androidonlineshop.androidonlineshop.db.async.category;

import android.database.sqlite.SQLiteConstraintException;
import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;
import java.util.Locale;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class CreateCategory extends AsyncTask<CategoryEntity, Void, Boolean> {

    private final WeakReference<View> mView;

    public CreateCategory(View view) {
        mView = new WeakReference<>(view);
    }

    @Override
    protected Boolean doInBackground(CategoryEntity... params) {
        DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
        boolean response = true;
        try {
            for (CategoryEntity category : params)
                dbCreator.getDatabase().categoryDAO().insertCategory(category);

        } catch (SQLiteConstraintException e) {
            response = false;
        }
        return response;
    }
}
