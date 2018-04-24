package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    private TextView itemName;
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

        itemName = view.findViewById(R.id.itemName);
        itemDescription = view.findViewById(R.id.itemDescription);
        itemRatingBar = view.findViewById(R.id.itemRatingBar);
        addToCartButton = view.findViewById(R.id.addToCartButton);
        itemPrice = view.findViewById(R.id.itemPrice);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            nameOfItem = (String) bundle.getSerializable("itemName");
        }

        try{
            cart = new GetCart(getView()).execute().get();
            item = new GetItem(getView()).execute(nameOfItem).get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }


        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemRatingBar.setRating(item.getRating());
        itemPrice.setText(item.getPrice()+"");

        addToCartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                item.setCartid(cart.getId());
                cart.setQuantity(cart.getQuantity()+1);
                try {
                   new UpdateCart(getView()).execute(cart).get();
                   new UpdateItem(getView()).execute(item).get();

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Toast.makeText(getContext(), "Item has been added to the cart!", Toast.LENGTH_LONG).show();

                MainFragment mainFragment = new MainFragment();
                Bundle bundle = new Bundle();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment, BACK_STACK_ROOT_TAG)
                        .commit();

            }
        });

    }


}
