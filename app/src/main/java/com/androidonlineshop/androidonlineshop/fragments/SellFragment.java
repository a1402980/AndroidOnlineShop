package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.category.GetCategories;
import com.androidonlineshop.androidonlineshop.db.async.item.CreateItem;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

import java.util.ArrayList;
import java.util.List;


public class SellFragment extends Fragment {

    // necessary fields to sell an item
    private EditText saleItemName;
    private EditText saleItemPrice;
    private RatingBar saleItemRatingBar;
    private Button saleItemButton;
    private EditText saleItemDescription;


    // dropdown spinner where the user can choose in which category to sell the item
    private Spinner itemCategories;

    // lists of categories and their names
    private List<CategoryEntity> categories;
    private List<String> categoryNames;

    // an item object
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

        // get all the fields from the view
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

        // instantiate the lists for categories and their names
        categories = new ArrayList<>();
        categoryNames = new ArrayList<>();

        // get all categories asynchronously from the database
        try{
            categories = new GetCategories(getView()).execute().get();

            // for every categories you retrieved add their names to the category names list
            for(CategoryEntity category : categories)
            {
                categoryNames.add(category.getName());
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // create an adapter for the spinner that will handle the category names
        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_spinner_item, categoryNames);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        itemCategories.setAdapter(adapter);

        // listen if the sell item button is being clicked
        saleItemButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // get the string values from the text fields of what user inputs
                String itemName = saleItemName.getText().toString();
                String itemDescription = saleItemDescription.getText().toString();

                // check if the price field of the item is not empty and then set the double value to what the user inputs
                double price = 0;
                if(!saleItemPrice.getText().toString().isEmpty()) {
                     price = Double.valueOf(saleItemPrice.getText().toString());
                }
                // get the rating from what user inputs
                int rating = Math.round(saleItemRatingBar.getRating());

                // set an integer position based on which category is being selected from the drop down list
                int position = itemCategories.getSelectedItemPosition();

                // do some checking if the fields are empty, then create a new item and put it in the sale list
                if(!itemName.isEmpty() && !itemDescription.isEmpty() && price > 0 && rating > 0) {
                    item = new ItemEntity(itemName, price, itemDescription, rating, 0, position+1, false);
                    try {
                        // create the item asychnronously
                        new CreateItem(getView()).execute(item).get();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    // notify the user that they have put an item for sale
                    Toast.makeText(getContext(), getString(R.string.lang_item_for_sale), Toast.LENGTH_LONG).show();


                    //reset fragment if you want to sell more
                    getActivity().getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    SellFragment sellFragment = new SellFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, sellFragment, BACK_STACK_ROOT_TAG)
                            .addToBackStack("stuff")
                            .commit();
                }
                else // if the fields are empty let the user know he/she needs to fill them
                {
                    Toast.makeText(getContext(), getString(R.string.lang_empty_fields), Toast.LENGTH_LONG).show();
                }

            }
        });

    }
}
