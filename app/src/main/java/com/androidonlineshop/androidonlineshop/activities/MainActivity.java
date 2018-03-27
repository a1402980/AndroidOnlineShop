package com.androidonlineshop.androidonlineshop.activities;


import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.model.Category;
import com.androidonlineshop.androidonlineshop.model.Item;
import com.androidonlineshop.androidonlineshop.room.Database;

public class MainActivity extends AppCompatActivity {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "onlineshop").build();

        Item item = new Item();
        item.setName("ttette");
        item.setDescription("tetetete");
        item.setPrice(5.00);
        item.setRating(5);


        db.itemDAO().insertItem(item);
    }
    public void testing()
    {
        Item item = new Item();
        item.setName("ttette");
        item.setDescription("tetetete");
        item.setPrice(5.00);
        item.setRating(5);


        db.itemDAO().insertItem(item);
    }
}
