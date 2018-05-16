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
import com.androidonlineshop.androidonlineshop.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.entity.ItemEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ShoppingCartFragment extends Fragment {

    // list view and other fields necessary for shopping cart
    private ListView cartItems;
    private TextView totalPrice;
    private Button buyItems;
    private TextView qtyNumber;
    private List<String> itemNames;
    private List<ItemEntity> items;
    private ItemEntity item;
    private CartEntity cart;
    private double priceTotal;
    private int quantity;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shopping_cart, container, false);

        // get the fields from the view
        cartItems = view.findViewById(R.id.cartItem);
        totalPrice = view.findViewById(R.id.totalPrice);
        buyItems = view.findViewById(R.id.buyItemsFromCart);
        qtyNumber = view.findViewById(R.id.qtyNumber);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        itemNames = new ArrayList<>();
        items = new ArrayList<>();
        totalPrice.setText("");
        qtyNumber.setText("");

        // create an adapter to handle the itemnames list
        final ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
        adapter.notifyDataSetChanged();
        cartItems.setAdapter(adapter);

        // retrieve the items from the database that were added to the cart
        FirebaseDatabase.getInstance()
                .getReference("items")
                .orderByChild("name")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (ItemEntity itemEntity : toItems(dataSnapshot)) {
                                if (itemEntity.isSold()) {
                                    itemNames.add(itemEntity.getName());
                                    items.add(itemEntity);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });

        // retreive the cart from the database and update the totalprice and quantity textviews
        FirebaseDatabase.getInstance()
                .getReference("cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {

                            for (CartEntity cartEntity : toCart(dataSnapshot)) {
                                cart = cartEntity;
                            }
                            totalPrice.setText(String.valueOf(cart.getTotalPrice()));
                            qtyNumber.setText(String.valueOf(cart.getQuantity()));
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


        // show the total price and quantity of items in the text view
        // listen if the items in the cart are being clicked for a longer period of time
        cartItems.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                // get the position of the item clicked
                item = items.get(pos);
                Log.v("long clicked","pos: " + pos);
                generateDialog(1);
                return true;
            }
        });

        // listen for the click of the button buy items
        buyItems.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // as long as there are items in the cart and you click buy items button remove them from database
                // because you bough the items they are no long in the store
                for(ItemEntity item : items)
                {
                    FirebaseDatabase.getInstance()
                            .getReference("items").child(item.getUid()).removeValue();
                }
                // update the cart after the items have been baught
                cart.setQuantity(0);
                cart.setTotalPrice(0);
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(cart.getUid())
                        .updateChildren(cart.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                } else {

                                }
                                adapter.notifyDataSetChanged();
                            }
                        });
                Toast.makeText(getActivity(), getString(R.string.lang_items_bought), Toast.LENGTH_LONG).show();
                refreshFragment();

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


                    // remove the items from the cart and update the item
                    item.setSold(false);
                    FirebaseDatabase.getInstance()
                            .getReference("items")
                            .child(item.getUid())
                            .updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(getActivity(), getString(R.string.lang_item_removed_cart), Toast.LENGTH_LONG).show();
                                    } else {
                                    }
                                }
                            });

                    // update the cart if an item is removed from it
                    cart.setTotalPrice(cart.getTotalPrice()-item.getPrice());
                    cart.setQuantity(cart.getQuantity()-1);
                    FirebaseDatabase.getInstance()
                            .getReference("cart")
                            .child(cart.getUid())
                            .updateChildren(cart.toMap(), new DatabaseReference.CompletionListener() {
                                @Override
                                public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                    if (databaseError != null) {
                                        Toast.makeText(getActivity(), getString(R.string.lang_item_removed_cart), Toast.LENGTH_LONG).show();
                                    } else {
                                    }
                                }
                            });
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
    // helper method to get the cart from firebase database
    private List<CartEntity> toCart(DataSnapshot snapshot)
    {
        List<CartEntity> carts = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren())
        {
            CartEntity cartEntity = childSnapshot.getValue(CartEntity.class);
            cartEntity.setUid(childSnapshot.getKey());
            carts.add(cartEntity);
        }
        return carts;
    }
}
