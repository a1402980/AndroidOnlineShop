package com.androidonlineshop.androidonlineshop;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.androidonlineshop.androidonlineshop.dal.ItemDAO;
import com.androidonlineshop.androidonlineshop.model.Item;
import com.androidonlineshop.androidonlineshop.room.Database;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;


/**
 * Created by ibraa on 27-Mar-18.
 */

@RunWith(AndroidJUnit4.class)
public class ItemDatabaseTest {
    private ItemDAO itemDAO;
    private Database db;

    @Before
    public void createDb() {
        Context context = InstrumentationRegistry.getTargetContext();
        db = Room.inMemoryDatabaseBuilder(context, Database.class).build();
        itemDAO = db.itemDAO();
    }

    @After
    public void closeDb() throws IOException {
        db.close();
    }

    @Test
    public void writeUserAndReadInList() throws Exception {
        Item item = new Item();
        item.setName("TEST");
        item.setDescription("OK");
        item.setPrice(5.00);
        item.setRating(5);
        itemDAO.insertItem(item);
        Item byName = itemDAO.findByName("TEST");
        assertEquals(byName.getName(), item.getName());
    }
}

