package com.androidonlineshop.androidonlineshop.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.activities.MainActivity;
import com.androidonlineshop.androidonlineshop.db.DatabaseCreator;


public class MainFragment extends Fragment {


    public MainFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.app_name));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main, container, false);

        /*final DatabaseCreator databaseCreator = DatabaseCreator.getInstance(getContext());
        Button generateData = view.findViewById(R.id.generateDataButton);
        generateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseCreator.createDb(getContext());
            }
        });*/

        return view;

    }


    @Override
    public void onResume(){
        super.onResume();

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.app_name));

        //set navigation focus on hidden item so previous page is not in focus
        NavigationView navigationView = getActivity().findViewById(R.id.navigation);
        navigationView.setCheckedItem(R.id.hidden);

    }




}
