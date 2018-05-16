package com.androidonlineshop.androidonlineshop;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Ibrahim Beqiri on 16-May-18.
 */

public class FirebasePersistenceHandler extends Application {
    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
