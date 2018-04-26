package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.cart.GetCart;
import com.androidonlineshop.androidonlineshop.db.async.cart.UpdateCart;
import com.androidonlineshop.androidonlineshop.db.async.item.GetItem;
import com.androidonlineshop.androidonlineshop.db.async.item.UpdateItem;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;


public class ItemFragment extends Fragment {

    // necessary fields to show the item price, description, rating, name and add them to cart
    private TextView itemName;
    private  TextView itemCategory;
    private TextView itemDescription;
    private RatingBar itemRatingBar;
    private Button addToCartButton;
    private ItemEntity item;
    private String nameOfItem;
    private CartEntity cart;
    private TextView itemPrice;

    private final String BACK_STACK_ROOT_TAG = "MAIN";

    public ItemFragment() {
        // Required empty public constructor
    }



    // TODO: Rename and change types and number of parameters
    public static ItemFragment newInstance() {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_menu_buy_items));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_item, container, false);

        // get all the fields from the view
        itemName = view.findViewById(R.id.itemName);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemRatingBar = view.findViewById(R.id.itemRatingBar);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        itemPrice = view.findViewById(R.id.itemPrice);
        itemCategory = view.findViewById(R.id.itemCategory);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the bundle from the previous fragment
        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            // get the name of the item that was clicked on the previous fragment
            nameOfItem = (String) bundle.getSerializable("itemName");
        }

        // try and catch error handling for asynchronous tasks
        try{
            // get the cart and the item based on the name asynchronously
            cart = new GetCart(getView()).execute().get();
            item = new GetItem(getView()).execute(nameOfItem).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        // set the fields based on the retrieved item's values
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemRatingBar.setRating(item.getRating());
        itemPrice.setText(item.getPrice()+"");
        //itemCategory.setText();

        // listenes if the add to cart button is clicked
        addToCartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // if the add to cart button is clicked then set the card id of the item to the id of the retrieved cart and set the boolean value as true
                item.setCartid(cart.getId());
                item.setSold(true);

                // a new item has been added to the cart so set the quantity to be +1
                cart.setQuantity(cart.getQuantity()+1);

                try {
                    // update the cart and the item asychnronously
                   new UpdateCart(getView()).execute(cart).get();
                   new UpdateItem(getView()).execute(item).get();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                // notify the user that the item has been added to the cart
                Toast.makeText(getContext(), "Item has been added to the cart!", Toast.LENGTH_LONG).show();

                // redirect back to the main fragment
                MainFragment mainFragment = new MainFragment();
                Bundle bundle = new Bundle();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("items")
                        .commit();

            }
        });

    }


}
