package com.androidonlineshop.androidonlineshop.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.entity.CartEntity;
import com.androidonlineshop.androidonlineshop.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.entity.ItemEntity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class ItemFragment extends Fragment {

    // necessary fields to show the item price, description, rating, name and add them to cart
    private TextView itemName;
    private  TextView itemCategory;
    private TextView itemDescription;
    private RatingBar itemRatingBar;
    private Button addToCartButton;
    private ItemEntity item;
    private TextView itemPrice;
    private CartEntity cart;
    private ImageView itemPic;
    private StorageReference mStorageRef;

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
        mStorageRef = FirebaseStorage.getInstance().getReference();
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
        itemPic = view.findViewById(R.id.itemImg);

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
            item = (ItemEntity) bundle.getSerializable("item");
        }

        // initiate the categories list
        final List<CategoryEntity> categories = new ArrayList<>();

        // retrieve all the categories from the database
        FirebaseDatabase.getInstance()
                .getReference("categories")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            for(CategoryEntity category : toCategories(dataSnapshot)) {
                                categories.add(category);
                                if(item.getCategoryid().equals(category.getUid())){
                                    itemCategory.setText(category.getName());
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

        // retrieve the cart from the databasse
        FirebaseDatabase.getInstance()
                .getReference("cart")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()) {

                            for(CartEntity cartEntity : toCart(dataSnapshot)) {
                                cart = cartEntity;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
        // set the fields based on the retrieved item's values
        itemName.setText(item.getName());
        itemDescription.setText(item.getDescription());
        itemRatingBar.setRating(item.getRating());
        itemPrice.setText(item.getPrice()+"");

        //get img base64
        mStorageRef.child(item.getUid()).getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap decodedByte = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                itemPic.setImageBitmap(decodedByte);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.e("Error", e.getMessage());
            }
        });
        // listenes if the add to cart button is clicked
        addToCartButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                // if the item is added to cart set it as sold and update it in the database
                item.setSold(true);
                FirebaseDatabase.getInstance()
                        .getReference("items")
                        .child(item.getUid())
                        .updateChildren(item.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                    Toast.makeText(getActivity(), getString(R.string.lang_item_added_to_cart), Toast.LENGTH_LONG).show();

                                } else {
                                }
                            }
                        });
                // update the quantity and the totalprice of the cart if items are added to it
                int quantity = 1;
                quantity = quantity + cart.getQuantity();
                cart.setQuantity(quantity);
                cart.setTotalPrice(cart.getTotalPrice()+item.getPrice());
                FirebaseDatabase.getInstance()
                        .getReference("cart")
                        .child(cart.getUid())
                        .updateChildren(cart.toMap(), new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                if (databaseError != null) {
                                } else {

                                }
                            }
                        });

                MainFragment mainFragment = new MainFragment();
                Bundle bundle = new Bundle();

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, mainFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("items")
                        .commit();


            }
        });

    }
    // helper method to get all the categories from firebase database
    private List<CategoryEntity> toCategories(DataSnapshot snapshot)
    {
        List<CategoryEntity> categories = new ArrayList<>();
        for(DataSnapshot childSnapshot : snapshot.getChildren())
        {
            CategoryEntity category = childSnapshot.getValue(CategoryEntity.class);
            category.setUid(childSnapshot.getKey());
            categories.add(category);
        }
        return categories;
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
