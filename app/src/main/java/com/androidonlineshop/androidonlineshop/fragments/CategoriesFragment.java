package com.androidonlineshop.androidonlineshop.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.category.GetCategories;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoriesFragment extends Fragment {


    private final String BACK_STACK_ROOT_TAG = "MAIN";


    private ListView categoriesListView;
    private List<CategoryEntity> categories;

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
            for(CategoryEntity category : categories)
            {
                categoryNames.add(category.getName());
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

                FragmentManager fragmentManager = getFragmentManager();
                BuyFragment buyFragment = new BuyFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id", position);
                buyFragment.setArguments(bundle);
                fragmentManager.beginTransaction().replace(R.id.fragment_container, buyFragment, BACK_STACK_ROOT_TAG).commit();
            }

        });

        Button addbutton = (Button) view.findViewById(R.id.addCat);
        addbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog();
            }
        });

        Button removebutton = (Button) view.findViewById(R.id.removeCat);
        removebutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showDialog();
            }
        });


    }

    protected void showDialog(){

        final Dialog dialog = new Dialog(getActivity());
        dialog.setCancelable(true);

        View view  = getActivity().getLayoutInflater().inflate(R.layout.prompt, null);
        dialog.setContentView(view);

        dialog.setTitle("Category stuff");

        TextView edit = (TextView) view.findViewById(R.id.prompt_accept);
        TextView delete = (TextView) view.findViewById(R.id.prompt_cancel);


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Do something

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();

            }
        });


        dialog.show();
    };
}
