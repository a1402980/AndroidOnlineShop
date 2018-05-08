package com.androidonlineshop.androidonlineshop.activities;

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

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.entity.ItemEntity;
import com.androidonlineshop.androidonlineshop.fragments.AboutFragment;
import com.androidonlineshop.androidonlineshop.fragments.BuyFragment;
import com.androidonlineshop.androidonlineshop.fragments.CategoriesFragment;
import com.androidonlineshop.androidonlineshop.fragments.MainFragment;
import com.androidonlineshop.androidonlineshop.fragments.SellFragment;
import com.androidonlineshop.androidonlineshop.fragments.SettingsFragment;
import com.androidonlineshop.androidonlineshop.fragments.ShoppingCartFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity{

    private final String BACK_STACK_ROOT_TAG = "MAIN";
    private DatabaseReference mDatabase;
    private FirebaseDatabase firebaseDatabase;
    private List<CategoryEntity> categories;
    private List<ItemEntity> items;
    private CartEntity cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        NavigationView nvDrawer = (NavigationView) findViewById(R.id.navigation);
        drawerSetup(nvDrawer);


        // getting the reference from database and enabling offline persistence mode
        firebaseDatabase = FirebaseDatabase.getInstance();
        firebaseDatabase.setPersistenceEnabled(true);
        mDatabase = firebaseDatabase.getReference();
        mDatabase.keepSynced(true);

        generateData();
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
            //if there is no other fragment in use, use the mainFragment
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

        Class fragmentClass = null;
        Fragment fragment = null;

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);

        switch (menuItem.getItemId()){
            case R.id.buy:
                fragmentClass = BuyFragment.class;
                break;
            case R.id.sell:
                fragmentClass = SellFragment.class;
                break;

            case R.id.shoppingCart:
                fragmentClass = ShoppingCartFragment.class;
                break;

            case R.id.categories:
                fragmentClass = CategoriesFragment.class;
                break;

            case R.id.settings:
                fragmentClass = SettingsFragment.class;
                break;

            case R.id.about:
                fragmentClass = AboutFragment.class;
                break;

            default:

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
    private void generateData()
    {
        categories = new ArrayList<>();
        items = new ArrayList<>();

        // creating some custom categories
        CategoryEntity accessories = new CategoryEntity(UUID.randomUUID().toString(),"Accessories", "In this category belong accessories!");
        CategoryEntity laptops = new CategoryEntity(UUID.randomUUID().toString(), "Laptop", "In this category belong laptops!");
        CategoryEntity TVs = new CategoryEntity(UUID.randomUUID().toString(), "TVs", "In this category belong TVs!");
        CategoryEntity phones = new CategoryEntity(UUID.randomUUID().toString(), "Phones", "In this category belong phones");
        CategoryEntity printers = new CategoryEntity(UUID.randomUUID().toString(), "Printers", "In this category belong printers");
        CategoryEntity tablets = new CategoryEntity(UUID.randomUUID().toString(), "Tablets", "In this category belong tablets");
        CategoryEntity electronics = new CategoryEntity(UUID.randomUUID().toString(), "Electronics", "In this category belong electronics");

        categories.add(accessories);
        categories.add(laptops);
        categories.add(TVs);
        categories.add(phones);
        categories.add(printers);
        categories.add(tablets);
        categories.add(electronics);

        cart = new CartEntity();
        cart.setUid(UUID.randomUUID().toString());
        cart.setQuantity(0);

        ItemEntity lenovo = new ItemEntity(UUID.randomUUID().toString(),"Lenovo Laptop", 565.00, "New laptop.", 5, cart.getUid(), laptops.getUid(), false);
        ItemEntity hp = new ItemEntity(UUID.randomUUID().toString(),"HP Laptop", 450.00, "New laptop.", 4, cart.getUid(), laptops.getUid(), false);
        ItemEntity headphones = new ItemEntity(UUID.randomUUID().toString(),"JBL HeadPhones", 45.00, "New headphones.", 5, cart.getUid(), accessories.getUid(), false);
        ItemEntity phonecase = new ItemEntity(UUID.randomUUID().toString(),"iphone 7 case", 15.00, "Phone Case for iphone 7", 3, cart.getUid(), accessories.getUid(), false);
        ItemEntity panasonicTV = new ItemEntity(UUID.randomUUID().toString(),"Panasonic 55' 4k TV",899.00,"This TV is in great condition. I bought it one year ago",5,cart.getUid(),TVs.getUid(), false);
        ItemEntity iphone = new ItemEntity(UUID.randomUUID().toString(),"iphone 5s",399.00,"The phone is cracked a little bit but works just fine",2,cart.getUid(),phones.getUid(), false);
        ItemEntity hpprinter = new ItemEntity(UUID.randomUUID().toString(),"HP PRinter",49.95,"Used printer for 2 years and now I have a new one. Works just fine. Has some colors included.",4,cart.getUid(),printers.getUid(), true);
        ItemEntity asuslaptop = new ItemEntity(UUID.randomUUID().toString(),"Asus Laptop", 565.00, "New laptop.", 5, cart.getUid(), laptops.getUid(), false);
        ItemEntity intelprocessor = new ItemEntity(UUID.randomUUID().toString(),"Intel processor i3", 52.95, "Intel processor i3. New.", 4, cart.getUid(), electronics.getUid(), false);
        ItemEntity simpleprinter = new ItemEntity(UUID.randomUUID().toString(),"Office Printer", 145.50, "New office printer.", 5, cart.getUid(), printers.getUid(), false);
        ItemEntity samsungtablet = new ItemEntity(UUID.randomUUID().toString(),"Samsung note tablet", 155.00, "Used Samsung tablet", 3, cart.getUid(), tablets.getUid(), false);
        ItemEntity samsunggalaxy= new ItemEntity(UUID.randomUUID().toString(),"Samsung galaxy s7",399.00,"New Samsung Galaxy s7. Earphones and charger included",5,cart.getUid(),phones.getUid(), false);
        ItemEntity nexus5 = new ItemEntity(UUID.randomUUID().toString(),"Nexus 5X phone",299.00,"The phone is used but there are not scratches.",4,cart.getUid(),phones.getUid(), false);

        items.add(lenovo);
        items.add(hp);
        items.add(headphones);
        items.add(phonecase);
        items.add(panasonicTV);
        items.add(iphone);
        items.add(hpprinter);
        items.add(asuslaptop);
        items.add(intelprocessor);
        items.add(simpleprinter);
        items.add(samsunggalaxy);
        items.add(samsungtablet);
        items.add(nexus5);

        mDatabase.child("categories").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    for(CategoryEntity categoryEntity : categories)
                    {
                        mDatabase.child("categories").child(categoryEntity.getUid()).setValue(categoryEntity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        mDatabase.child("items").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists())
                {
                    for(ItemEntity itemEntity : items)
                    {
                        mDatabase.child("items").child(itemEntity.getUid()).setValue(itemEntity);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
