package com.androidonlineshop.androidonlineshop.fragments;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.entity.ItemEntity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class BuyFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private ListView itemsListView;
    private List<ItemEntity> items;
    private ItemEntity item;
    private CategoryEntity category;

    private final String BACK_STACK_ROOT_TAG = "MAIN";


    public BuyFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuyFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuyFragment newInstance(String param1, String param2) {
        BuyFragment fragment = new BuyFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_menu_buy_items));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buy, container, false);

        // get the listview from the view
        itemsListView = view.findViewById(R.id.itemsListView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // get the bundle from the fragment arguments
        Bundle bundle = getArguments();
        if(bundle != null)
        {
            // get category from the bundle that instantiated the fragment previously
            category = (CategoryEntity) bundle.getSerializable("category");
        }

        // initialize the items list
        items = new ArrayList<>();
        // create a lsit for items names
        final List<String> itemNames = new ArrayList<>();

        // create an adapter that will handle the items in the list view
        final ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
        itemsListView.setAdapter(adapter);

        // retrieving all items from firebase database
        FirebaseDatabase.getInstance()
                .getReference("items")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            // if the user navigated from category then only show items that belong to a category
                            if(category != null) {
                                for (ItemEntity itemEntity : toItems(dataSnapshot)) {
                                    if (itemEntity.getCategoryid().equals(category.getUid()) && !itemEntity.isSold()) {
                                        itemNames.add(itemEntity.getName());
                                        items.add(itemEntity);
                                    }
                                }
                            }
                            else // if the user navigated directly form buy items page then show all items
                            {
                                for (ItemEntity itemEntity : toItems(dataSnapshot)) {
                                    if (!itemEntity.isSold()) {
                                        itemNames.add(itemEntity.getName());
                                        items.add(itemEntity);
                                    }
                                }
                            }
                            // notify the adapter that the data has changed
                            adapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // if the item is clicked get the itemName and instantiante new ItemFragment and replace the current one
        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ItemFragment itemFragment = new ItemFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", items.get(position));
                itemFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, itemFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("categories")
                        .commit();
            }

        });

        // if the item is clicked for a long time then generate the below dialog
        itemsListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int pos, long id) {
                // TODO Auto-generated method stub
                // get the clicked item
                item = items.get(pos);
                Log.v("long clicked","pos: " + pos);
                generateDialog(1);
                return true;
            }
        });

        // this button redirects to SellFragment
        Button addbutton = (Button) view.findViewById(R.id.sellItem);
        addbutton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                SellFragment sellFragment = new SellFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, sellFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("items")
                        .commit();
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }

    // generate dialogs for long click of an item
    private void generateDialog(final int action) {
        LayoutInflater inflater = LayoutInflater.from(getContext());

        final View view = inflater.inflate(R.layout.prompt, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();



        if (action == 1){

            alertDialog.setTitle(getString(R.string.lang_modify_delete));
            alertDialog.setCancelable(true);

            // if the user modifies the item generate dialog 2
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_modify), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    generateDialog(2);

                }
            });
            // if the user deletes the item run the asynchronous taks do delete the item
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.lang_delete), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // remove the item the user wants from the database
                    FirebaseDatabase.getInstance()
                            .getReference("items").child(item.getUid()).removeValue();

                    refreshFragment();
                }
            });

        }else if(action == 2){ // create a new layot if the user decided to modify the item

            alertDialog.setTitle(getString(R.string.lang_modify));
            alertDialog.setCancelable(true);

            // textviews and edit texts inputs for all the fields of the item (name, description, price, rating)
            LinearLayout layout       = new LinearLayout(getActivity());
            TextView tvMessage        = new TextView(getActivity());
            TextView tvMessage2        = new TextView(getActivity());
            TextView tvMessage3        = new TextView(getActivity());
            TextView tvMessage4        = new TextView(getActivity());
            final EditText etInput    = new EditText(getActivity());
            final EditText etInput2    = new EditText(getActivity());
            final EditText etInput3   = new EditText(getActivity());
            final EditText etInput4   = new EditText(getActivity());

            // se the edittext values to the items real values
            etInput.setText(item.getName());
            etInput2.setText(item.getDescription());
            etInput3.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etInput4.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            etInput3.setText(String.valueOf(item.getPrice()));
            etInput4.setText(String.valueOf(item.getRating()));

            // set the hint for text fields
            etInput.setHint(getString(R.string.lang_name));
            etInput2.setHint(getString(R.string.lang_description));
            etInput3.setHint(getString(R.string.lang_price));
            etInput4.setHint(getString(R.string.lang_item_condition));

            tvMessage.setText(getString(R.string.lang_name));
            tvMessage2.setText(getString(R.string.lang_description));
            tvMessage3.setText(getString(R.string.lang_price));
            tvMessage4.setText(getString(R.string.lang_item_condition));

            //SET VALUES FOR etInput & etInput2 here!

            //tvMessage.setText(getString(R.string.lang_modify_delete));
            //tvMessage.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f);
            etInput.setSingleLine();
            layout.setOrientation(LinearLayout.VERTICAL);
            //layout.addView(tvMessage);
            layout.addView(tvMessage);
            layout.addView(etInput);
            layout.addView(tvMessage2);
            layout.addView(etInput2);
            layout.addView(tvMessage3);
            layout.addView(etInput3);
            layout.addView(tvMessage4);
            layout.addView(etInput4);
            layout.setPadding(50, 40, 50, 10);

            alertDialog.setView(layout);

            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.lang_confirm), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    // create strings which get the text from text fields
                    String itemName = etInput.getText().toString();
                    String itemDescription = etInput2.getText().toString();
                    String itemPrice = etInput3.getText().toString();
                    String itemRating = etInput4.getText().toString();

                    double price = 0;
                    float rating = 0;
                    if(!itemPrice.isEmpty() && !itemRating.isEmpty())
                    {
                        price = Double.valueOf(itemPrice);
                        rating = Float.valueOf(itemRating);
                    }
                    item.setName(itemName);
                    item.setDescription(itemDescription);
                    item.setPrice(price);
                    item.setRating(rating);

                    // do some checking for the values and then update the item in database
                    if(!itemName.isEmpty() && !itemDescription.isEmpty() && price > 0 && rating > 0 && rating < 6) {
                        FirebaseDatabase.getInstance()
                                .getReference("items")
                                .child(item.getUid())
                                .updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
                                    @Override
                                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                        if (databaseError != null) {
                                        } else {
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(getContext(), getString(R.string.lang_rating_limit), Toast.LENGTH_LONG).show();
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
        }
        alertDialog.show();

    }
    private void refreshFragment()
    {
        BuyFragment buyFragment = new BuyFragment();
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, buyFragment, BACK_STACK_ROOT_TAG)
                .addToBackStack("items")
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

}
