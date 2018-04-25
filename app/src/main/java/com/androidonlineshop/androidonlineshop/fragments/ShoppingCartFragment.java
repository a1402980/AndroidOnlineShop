package com.androidonlineshop.androidonlineshop.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.cart.GetCartWithItems;
import com.androidonlineshop.androidonlineshop.db.async.item.DeleteItem;
import com.androidonlineshop.androidonlineshop.db.async.item.UpdateItem;
import com.androidonlineshop.androidonlineshop.db.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;
import com.androidonlineshop.androidonlineshop.db.pojo.CartWithItems;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {

    private ListView cartItems;
    private TextView totalPrice;
    private Button buyItems;
    private TextView qtyNumber;
    private List<String> itemNames;
    private List<CartWithItems> cartWithItemsList;
    private List<ItemEntity> items;
    private ItemEntity item;
    private int itemPosition;

    private final String BACK_STACK_ROOT_TAG = "MAIN";

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

        cartItems = view.findViewById(R.id.cartItem);
        totalPrice = view.findViewById(R.id.totalPrice);
        buyItems = view.findViewById(R.id.buyItemsFromCart);
        qtyNumber = view.findViewById(R.id.qtyNumber);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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
        double priceTotal = 0;
        for(ItemEntity item : items)
        {
                priceTotal += item.getPrice();
        }

        totalPrice.setText(String.valueOf(priceTotal));
        qtyNumber.setText(String.valueOf(items.size()));

        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
        cartItems.setAdapter(adapter);

        cartItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                itemPosition = pos;
                Log.v("long clicked","pos: " + pos);
                generateDialog(1);
                return true;
            }
        });

        buyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!itemNames.isEmpty()) {
                    for (ItemEntity item : items) {
                        try {
                            new DeleteItem(getView()).execute(item).get();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    Toast.makeText(getActivity(), getString(R.string.lang_items_bought), Toast.LENGTH_LONG).show();
                    getActivity().getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                    MainFragment mainFragment = new MainFragment();
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, mainFragment, BACK_STACK_ROOT_TAG)
                            .addToBackStack("main")
                            .commit();
                }
                else
                {
                    Toast.makeText(getActivity(), getString(R.string.lang_empty_cart), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private void generateDialog(final int action) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        final View view = inflater.inflate(R.layout.prompt, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

        if (action == 1){

            alertDialog.setTitle(getString(R.string.lang_delete) + "?");
            alertDialog.setCancelable(true);


            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    alertDialog.dismiss();
                }
            });

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    item = items.get(itemPosition);
                    item.setCartid(0);
                    item.setSold(false);
                    try
                    {
                        new UpdateItem(getView()).execute(item).get();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                    refreshFragment();
                }
            });

        }

        alertDialog.show();

    }
    private void refreshFragment()
    {
        getActivity().getSupportFragmentManager().popBackStack(BACK_STACK_ROOT_TAG, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        ShoppingCartFragment shoppingCartFragment = new ShoppingCartFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, shoppingCartFragment, BACK_STACK_ROOT_TAG)
                .addToBackStack("main")
                .commit();
    }
}
