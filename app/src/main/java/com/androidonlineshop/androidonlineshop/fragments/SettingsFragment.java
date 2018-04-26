package com.androidonlineshop.androidonlineshop.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Locale;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.widget.ImageButton;
import com.androidonlineshop.androidonlineshop.R;


public class SettingsFragment extends Fragment {



    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //set page title from strings
        getActivity().setTitle(getResources().getText(R.string.lang_menu_settings));
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set on click listeners fo the flag buttons
        View view = inflater.inflate(R.layout.fragment_settings,
                container, false);

        ImageButton buttonEng = (ImageButton) view.findViewById(R.id.lang_eng);
        buttonEng.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String lang = "eng";
                setLocale(lang);
            }
        });

        ImageButton buttonDe = (ImageButton) view.findViewById(R.id.lang_de);
        buttonDe.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String lang = "de";
                setLocale(lang);
            }
        });

        return view;
    }



    public void setLocale(String lang) {
        //change the apps default language to a different one so the language will change.
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);

        getActivity().recreate();



    }




}
