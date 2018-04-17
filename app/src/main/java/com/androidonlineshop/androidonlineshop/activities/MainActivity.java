package com.androidonlineshop.androidonlineshop.activities;


import android.arch.persistence.room.Room;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.fragments.BuyFragment;
import com.androidonlineshop.androidonlineshop.fragments.SellFragment;
import com.androidonlineshop.androidonlineshop.model.Item;
import com.androidonlineshop.androidonlineshop.db.Database;

public class MainActivity extends AppCompatActivity implements BuyFragment.OnFragmentInteractionListener, SellFragment.OnFragmentInteractionListener {

    private Database db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = Room.databaseBuilder(getApplicationContext(),
                Database.class, "onlineshop").build();

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.navigation);
        drawerSetup(nvDrawer);

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
    private static Item addItem(final Database db, Item item) {
        db.itemDAO().insertItem(item);
        return item;
    }

    private static void populateWithTestData(Database db) {
        Item item = new Item();
        item.setName("ttette");
        item.setDescription("tetetete");
        item.setPrice(5.00);
        item.setRating(5);


        addItem(db, item);
    }

    public void startBuyFragment(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BuyFragment fragmentBuy = new BuyFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_container,fragmentBuy);
        fragmentTransaction.commit();
    }

    public void startSellFragment(View v) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        SellFragment fragmentBuy = new SellFragment();
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.fragment_container,fragmentBuy);
        fragmentTransaction.commit();
    }

    @Override
    public void onFragmentInteraction(Uri uri){
        //you can leave this empty. It is used to switch between fragments.
        //you must implement this class with all the fragments related to this activity.
    }



    public void selectItemDrawer(MenuItem menuItem){
        Fragment myFragment = null;

        View v = this.findViewById(android.R.id.content).getRootView();

        switch (menuItem.getItemId()){
            case R.id.buy:
                Toast.makeText(this, "buy", Toast.LENGTH_SHORT).show();
                startBuyFragment(v);

                break;

            case R.id.sell:
                Toast.makeText(this, "Sell", Toast.LENGTH_SHORT).show();
                startSellFragment(v);
                break;

            case R.id.shoppingCart:
                Toast.makeText(this, "Shopping cart", Toast.LENGTH_SHORT).show();

                break;

            case R.id.categories:

                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();

                break;



            default:
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);

    }
    private void drawerSetup(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                selectItemDrawer(item);
                return true;
            }
        });
    }
}
