package com.androidonlineshop.androidonlineshop.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.cart.UpdateCart;
import com.androidonlineshop.androidonlineshop.db.async.category.GetCategories;
import com.androidonlineshop.androidonlineshop.db.async.item.CreateItem;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;


public class SellFragment extends Fragment {


    private EditText saleItemName;
    private EditText saleItemPrice;
    private Spinner itemCategories;
    private RatingBar saleItemRatingBar;
    private Button saleItemButton;
    private EditText saleItemDescription;

    private List<CategoryEntity> categories;
    private List<String> categoryNames;
    private ItemEntity item;

    private final String BACK_STACK_ROOT_TAG = "MAIN";

    public SellFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_menu_sell_items));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sell, container, false);

        saleItemName = view.findViewById(R.id.saleItemName);
        saleItemPrice = view.findViewById(R.id.saleItemPrice);
        saleItemRatingBar = view.findViewById(R.id.saleRatingBar);
        saleItemButton = view.findViewById(R.id.saleItemButton);
        saleItemDescription = view.findViewById(R.id.saleItemDescription);
        itemCategories = view.findViewById(R.id.itemCategories);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        categories = new ArrayList<>();
        categoryNames = new ArrayList<>();
        try{
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

        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategories.setAdapter(adapter);

        saleItemButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                String itemName = saleItemName.getText().toString();
                String itemDescription = saleItemDescription.getText().toString();
                double price = 1;
                if(!saleItemPrice.getText().toString().isEmpty()) {
                     price = Double.valueOf(saleItemPrice.getText().toString());
                }
                int rating = Math.round(saleItemRatingBar.getRating());
                int position = itemCategories.getSelectedItemPosition();

                if(!itemName.isEmpty() && !itemDescription.isEmpty() && price > 0 && rating > 0) {
                    item = new ItemEntity(itemName, price, itemDescription, rating, 0, position+1, false);
                    try {
                        new CreateItem(getView()).execute(item).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getContext(), "You have put an item to sale!", Toast.LENGTH_LONG).show();


                    //reset fragment
                    SellFragment sellFragment = new SellFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, sellFragment, BACK_STACK_ROOT_TAG)
                            .addToBackStack("categories")
                            .commit();
                }
                else
                {
                    Toast.makeText(getContext(), "You need to fill all the fields in order to sell!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
