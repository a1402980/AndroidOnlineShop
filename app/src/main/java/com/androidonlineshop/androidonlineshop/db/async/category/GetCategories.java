package com.androidonlineshop.androidonlineshop.db.async.category;

import android.os.AsyncTask;
import android.view.View;

import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Ibrahim Beqiri on 17-Apr-18.
 */

public class GetCategories extends AsyncTask<Void, Void, List<CategoryEntity>> {

// Weak references will still allow the Activity to be garbage-collected
        private final WeakReference<View> mView;

        public GetCategories(View view) {
            mView = new WeakReference<>(view);
        }

        @Override
        protected List<CategoryEntity> doInBackground(Void... voids) {
            DatabaseCreator dbCreator = DatabaseCreator.getInstance(mView.get().getContext());
            return dbCreator.getDatabase().categoryDAO().getAllCategories();
            }
}
