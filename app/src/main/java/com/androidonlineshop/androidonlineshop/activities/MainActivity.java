package com.androidonlineshop.androidonlineshop.activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;
import com.androidonlineshop.androidonlineshop.fragments.AboutFragment;
import com.androidonlineshop.androidonlineshop.fragments.BuyFragment;
import com.androidonlineshop.androidonlineshop.fragments.CategoriesFragment;
import com.androidonlineshop.androidonlineshop.fragments.MainFragment;
import com.androidonlineshop.androidonlineshop.fragments.SellFragment;
import com.androidonlineshop.androidonlineshop.fragments.SettingsFragment;
import com.androidonlineshop.androidonlineshop.fragments.ShoppingCartFragment;

public class MainActivity extends AppCompatActivity{

    private final String BACK_STACK_ROOT_TAG = "MAIN";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.navigation);
        drawerSetup(nvDrawer);

        final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(this.getApplication());

        final Button generateData = findViewById(R.id.generateData);
        generateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                generateData.setVisibility(View.INVISIBLE);
                databaseCreator.createDb(getApplication());
            }
        });

        //setup toolbar with a button to open nav drawer
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
        actionbar.setDisplayHomeAsUpEnabled(true);



        if (savedInstanceState == null) {
            Fragment fragment = null;
            Class fragmentClass = MainFragment.class;
            try {
                fragment = (Fragment) fragmentClass.newInstance();
            } catch (Exception e) {
                Log.e("TAG", e.getMessage(), e);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment, BACK_STACK_ROOT_TAG).commit();
        }




    }

    @Override   //open navigation drawer with a button
    public boolean onOptionsItemSelected(MenuItem item) {
        final DrawerLayout mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void selectItemDrawer(MenuItem menuItem){

        View v = this.findViewById(android.R.id.content).getRootView();

        Class fragmentClass = null;
        Fragment fragment = null;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        switch (menuItem.getItemId()){
            case R.id.buy:
                Toast.makeText(this, "buy", Toast.LENGTH_SHORT).show();
                fragmentClass = BuyFragment.class;

                break;

            case R.id.sell:
                Toast.makeText(this, "Sell", Toast.LENGTH_SHORT).show();
                fragmentClass = SellFragment.class;
                break;

            case R.id.shoppingCart:
                Toast.makeText(this, "Shopping cart", Toast.LENGTH_SHORT).show();
                fragmentClass = ShoppingCartFragment.class;
                break;

            case R.id.categories:
                fragmentClass = CategoriesFragment.class;
                Toast.makeText(this, "Categories", Toast.LENGTH_SHORT).show();

                break;

            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();

                break;

            case R.id.about:
                fragmentClass = AboutFragment.class;
                Toast.makeText(this, "About", Toast.LENGTH_SHORT).show();

                break;



            default:
                Toast.makeText(this, "Default", Toast.LENGTH_SHORT).show();

        }

        try {

            fragment = (Fragment) fragmentClass.newInstance();

        }catch (Exception e){

        }

        fragmentManager.beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack(BACK_STACK_ROOT_TAG).commit();

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
