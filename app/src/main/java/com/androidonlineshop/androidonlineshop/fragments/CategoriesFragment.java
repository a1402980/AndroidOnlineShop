package com.androidonlineshop.androidonlineshop.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.cart.UpdateCart;
import com.androidonlineshop.androidonlineshop.db.async.category.CreateCategory;
import com.androidonlineshop.androidonlineshop.db.async.category.DeleteCategory;
import com.androidonlineshop.androidonlineshop.db.async.category.GetCategories;
import com.androidonlineshop.androidonlineshop.db.async.category.GetCategoriesWithItems;
import com.androidonlineshop.androidonlineshop.db.async.category.UpdateCategory;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CategoryWithItems;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {


    private final String BACK_STACK_ROOT_TAG = "MAIN";


    private ListView categoriesListView;
    private List<CategoryEntity> categories;
    private int categoryPosition;
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

        categoriesListView = view.findViewById(R.id.categoriesListView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        categories = new ArrayList<>();
        List<String> categoryNames = new ArrayList<>();


        // set the listview in the activity with the adapter
        try {
            categories = new GetCategories(getView()).execute().get();
            if(!categories.isEmpty()) {
                for (CategoryEntity category : categories) {
                    categoryNames.add(category.getName());
                }
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, categoryNames);
        categoriesListView.setAdapter(adapter);

        categoriesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {




                //FragmentManager fragmentManager = getFragmentManager();
                BuyFragment buyFragment = new BuyFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("category", categories.get(position));
                buyFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, buyFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("categories")
                        .commit();

                //fragmentManager.beginTransaction().replace(R.id.fragment_container, buyFragment, BACK_STACK_ROOT_TAG).commit();
            }

        });

        categoriesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                categoryPosition = pos;
                Log.v("long clicked","pos: " + pos);
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

        final View view = inflater.inflate(R.layout.prompt, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();



        if (action == 1){

            alertDialog.setTitle(getString(R.string.lang_modify_delete));
            alertDialog.setCancelable(true);


            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_modify), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                generateDialog(2);

                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    try{
                         new DeleteCategory(getView()).execute(categories.get(categoryPosition)).get();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    refreshFragment();
                }
            });

        }else if(action == 2){

            alertDialog.setTitle(getString(R.string.lang_modify));
            alertDialog.setCancelable(true);

            LinearLayout layout       = new LinearLayout(getActivity());
            TextView tvMessage        = new TextView(getActivity());
            TextView tvMessage2        = new TextView(getActivity());
            final EditText etInput    = new EditText(getActivity());
            final EditText etInput2    = new EditText(getActivity());

            category = categories.get(categoryPosition);
            etInput.setText(category.getName());
            etInput2.setText(category.getDescription());

            //etInput.setHint(getString(R.string.lang_name));
            //etInput2.setHint(getString(R.string.lang_description));

            //SET VALUES FOR etInput & etInput2 here!

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


                    String categoryName = etInput.getText().toString();
                    String categoryDescription = etInput2.getText().toString();

                    if(!categoryName.isEmpty() && !categoryDescription.isEmpty()) {
                        category.setName(categoryName);
                        category.setDescription(categoryDescription);
                        try {
                            new UpdateCategory(getView()).execute(category).get();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "Some fields are empty!", Toast.LENGTH_LONG);
                    }
                    refreshFragment();

                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
        }else if(action == 3){

            alertDialog.setTitle(getString(R.string.lang_add_category));
            alertDialog.setCancelable(true);

            LinearLayout layout       = new LinearLayout(getActivity());
            TextView tvMessage        = new TextView(getActivity());
            final EditText name    = new EditText(getActivity());
            final EditText description    = new EditText(getActivity());

            name.setHint(getString(R.string.lang_name));
            description.setHint(getString(R.string.lang_description));

            //tvMessage.setText(getString(R.string.lang_modify_delete));
            //tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            name.setSingleLine();
            layout.setOrientation(LinearLayout.VERTICAL);
            //layout.addView(tvMessage);
            layout.addView(name);
            layout.addView(description);
            layout.setPadding(50, 40, 50, 10);

            alertDialog.setView(layout);



            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String categoryName = name.getText().toString();
                    String categoryDescription = description.getText().toString();
                    if(!categoryName.isEmpty() && !categoryDescription.isEmpty()) {
                        CategoryEntity category = new CategoryEntity(categoryName, categoryDescription);
                        try {
                            boolean response = new CreateCategory(getView()).execute(category).get();
                            System.out.println("Category inserted? " + response);

                        } catch (Exception e) {

                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(), "Some fields are empty!", Toast.LENGTH_LONG);
                    }
                    //reset fragment
                    refreshFragment();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
        }

        alertDialog.show();

    }
    private void refreshFragment()
    {
        CategoriesFragment categoriesFragment = new CategoriesFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, categoriesFragment, BACK_STACK_ROOT_TAG)
                .addToBackStack("categories")
                .commit();
    }
}
