package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.androidonlineshop.androidonlineshop.R;
import com.androidonlineshop.androidonlineshop.db.async.item.GetItems;
import com.androidonlineshop.androidonlineshop.db.entity.CategoryEntity;
import com.androidonlineshop.androidonlineshop.db.entity.ItemEntity;

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

        itemsListView = view.findViewById(R.id.itemsListView);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Bundle bundle = this.getArguments();
        if(bundle != null)
        {
            category = (CategoryEntity) bundle.getSerializable("category");
        }

        items = new ArrayList<>();
        final List<String> itemNames = new ArrayList<>();

        try {
            items = new GetItems(getView()).execute().get();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        if(!items.isEmpty()) {
            if (category != null) {
                for (ItemEntity item : items) {
                    if (category.getId() == item.getCategoryid()) {
                        itemNames.add(item.getName());
                    }
                }
            } else {
                for (ItemEntity item : items) {
                    itemNames.add(item.getName());

                }
            }
        }
        ArrayAdapter<String> adapter = new ArrayAdapter(this.getContext(), android.R.layout.simple_list_item_1, itemNames);
        itemsListView.setAdapter(adapter);

        itemsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                FragmentManager fragmentManager = getFragmentManager();
                ItemFragment itemFragment = new ItemFragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("itemName", itemNames.get(position));
                System.out.println(items.get(position).getName());
                itemFragment.setArguments(bundle);

                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container, itemFragment, BACK_STACK_ROOT_TAG)
                        .addToBackStack("categories")
                        .commit();

                //fragmentManager.beginTransaction().replace(R.id.fragment_container, itemFragment, BACK_STACK_ROOT_TAG).commit();
            }

        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        super.onActivityCreated(savedInstanceState);
    }


}
