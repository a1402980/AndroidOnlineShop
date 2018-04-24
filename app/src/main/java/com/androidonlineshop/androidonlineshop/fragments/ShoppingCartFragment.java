package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.cart.GetCartWithItems;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CartWithItems;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {


    private CartEntity cart;
    private ItemEntity item;
    private ListView cartItems;
    private List<String> itemNames;
    private List<CartWithItems> cartWithItemsList;
    private List<ItemEntity> items;


    public ShoppingCartFragment() {
        // Required empty public constructor
    }

    public static ShoppingCartFragment newInstance(String param1, String param2) {
        ShoppingCartFragment fragment = new ShoppingCartFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_shopping_cart_title));
        cartWithItemsList = new ArrayList<>();
        itemNames = new ArrayList<>();
        items = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        cartItems = view.findViewById(R.id.cartItems);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /*Bundle bundle = this.getArguments();
        if(bundle != null)
        {
             cart = (CartEntity) bundle.getSerializable("cart");
             item = (ItemEntity) bundle.getSerializable("item");

        }

        if(item != null && cart != null) {
            itemNames.add(item.getName());
            ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
            cartItems.setAdapter(adapter);
        }*/

        try{
            cartWithItemsList = new GetCartWithItems(getView()).execute().get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        for(CartWithItems cartWithItems : cartWithItemsList)
        {
            for(ItemEntity itemEntity : cartWithItems.items)
            {
                items.add(itemEntity);
                itemNames.add(itemEntity.getName());
            }
        }

        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
        cartItems.setAdapter(adapter);


    }
}
