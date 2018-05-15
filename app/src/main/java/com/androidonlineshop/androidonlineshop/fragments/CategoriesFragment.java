package com.androidonlineshop.androidonlineshop.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.entity.ItemEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CategoriesFragment extends Fragment {


    private final String BACK_STACK_ROOT_TAG = "MAIN";


    private ListView categoriesListView;
    private List<CategoryEntity> categories;
    private CategoryEntity category;

    public CategoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_menu_categories));



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        //get listview from layout
        categoriesListView = view.findViewById(R.id.categoriesListView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // initiate the categories list and a list with categories names to show them in the adapter
        categories = new ArrayList<>();
        final List<String> categoryNames = new ArrayList<>();

        // create an adapter to show the list items
        final ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, categoryNames);
        adapter.notifyDataSetChanged();
        categoriesListView.setAdapter(adapter);

        // retrieve all the categories from the database
        FirebaseDatabase.getInstance()
                .getReference("categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            for(CategoryEntity category : toCategories(dataSnapshot)) {
                                categoryNames.add(category.getName());
                                categories.add(category);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //when clicking on an item on the list, take to the buy items page with only those categories items there
                BuyFragment buyFragment = new BuyFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", categories.get(position));
                buyFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, buyFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("categories")
                        .commit();

            }

        });

        categoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                // get the clicked category
                category = categories.get(pos);
                Log.v("long clicked","pos: " + pos);
                //when pressing down on an item on the list, generate a dialog that asks what user wants to do with the list
                generateDialog(1);
                return true;
            }
        });

        Button addbutton = (Button) view.findViewById(R.id.addCat);
        addbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                generateDialog(3);
            }
        });

    }

    private void generateDialog(final int action) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        //initialize the dialog
        final View view = inflater.inflate(R.layout.prompt, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();



        if (action == 1){
            //ask user what he wants to do with the selected item
            alertDialog.setTitle(getString(R.string.lang_modify_delete));
            alertDialog.setCancelable(true);


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_modify), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                //if user chooses modify, open a new dialog with different fields
                generateDialog(2);

                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // delete the category
                    FirebaseDatabase.getInstance()
                            .getReference("categories").child(category.getUid()).removeValue();

                    // delete all the items that belong to the category if  a category is deleted
                    FirebaseDatabase.getInstance()
                            .getReference("items")
                            .addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {

                                            for (ItemEntity item : toItems(dataSnapshot)) {
                                                if (item.getCategoryid().equals(category.getUid())) {
                                                    FirebaseDatabase.getInstance()
                                                            .getReference("items").child(item.getUid()).removeValue();
                                                }
                                            }
                                        }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });
                    refreshFragment();
                }
            });

        }else if(action == 2){
            //generate a dialog that has information about the category with the current data in the fields
            alertDialog.setTitle(getString(R.string.lang_modify));
            alertDialog.setCancelable(true);

            LinearLayout layout       = new LinearLayout(getActivity());
            TextView tvMessage        = new TextView(getActivity());
            TextView tvMessage2        = new TextView(getActivity());
            final EditText etInput    = new EditText(getActivity());
            final EditText etInput2    = new EditText(getActivity());

            etInput.setText(category.getName());
            etInput2.setText(category.getDescription());


            tvMessage.setText(getString(R.string.lang_name));
            tvMessage2.setText(getString(R.string.lang_description));

            etInput.setSingleLine();
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(tvMessage);
            layout.addView(etInput);
            layout.addView(tvMessage2);
            layout.addView(etInput2);
            layout.setPadding(50, 40, 50, 10);

            alertDialog.setView(layout);

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //when clicking confirm, update the fields

                    // get the calues from text fields
                    String categoryName = etInput.getText().toString();
                    String categoryDescription = etInput2.getText().toString();
                    category.setName(categoryName);
                    category.setDescription(categoryDescription);

                    // update the category with the new values
                    FirebaseDatabase.getInstance()
                            .getReference("categories")
                            .child(category.getUid())
                            .updateChildren(category.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if(databaseError != null)
                                    {
                                    }
                                    else
                                    {
                                    }
                                }
                            });

                    refreshFragment();

                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if cancel, close the dialog
                    alertDialog.dismiss();
                }
            });
        }else if(action == 3){

            //generate a dialog for making a new category

            alertDialog.setTitle(getString(R.string.lang_add_category));
            alertDialog.setCancelable(true);

            LinearLayout layout       = new LinearLayout(getActivity());
            final EditText name    = new EditText(getActivity());
            final EditText description    = new EditText(getActivity());

            name.setHint(getString(R.string.lang_name));
            description.setHint(getString(R.string.lang_description));

            name.setSingleLine();
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.addView(name);
            layout.addView(description);
            layout.setPadding(50, 40, 50, 10);

            alertDialog.setView(layout);

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //when clicking confirm, create a new category
                    String categoryName = name.getText().toString();
                    String categoryDescription = description.getText().toString();

                    final CategoryEntity category = new CategoryEntity(UUID.randomUUID().toString(), categoryName, categoryDescription);

                    // create a new category in the database
                    FirebaseDatabase.getInstance()
                            .getReference()
                            .child("categories").child(category.getUid()).setValue(category);
                    //reset fragment
                    refreshFragment();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //if cancel click, close dialog
                    alertDialog.dismiss();
                }
            });
        }

        alertDialog.show();

    }
    private void refreshFragment()
    {
        //this method is used so that the page is refreshed
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, categoriesFragment, BACK_STACK_ROOT_TAG)
                .addToBackStack("categories")
                .commit();
    }
    // helper method to get all the categories from firebase database
    private List<CategoryEntity> toCategories(DataSnapshot snapshot)
    {
        List<CategoryEntity> categories = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren())
        {
            CategoryEntity category = childSnapshot.getValue(CategoryEntity.class);
            category.setUid(childSnapshot.getKey());
            categories.add(category);
        }
        return categories;
    }
    // helper method to get all the items from firebase database
    private List<ItemEntity> toItems(DataSnapshot snapshot)
    {
        List<ItemEntity> items = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren())
        {
            ItemEntity item = childSnapshot.getValue(ItemEntity.class);
            item.setUid(childSnapshot.getKey());
            items.add(item);
        }
        return items;
    }
}
