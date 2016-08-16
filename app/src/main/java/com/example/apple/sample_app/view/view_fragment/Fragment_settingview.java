package com.example.apple.sample_app.view.view_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_settingview extends Fragment {


    public Fragment_settingview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settingview, container, false);

        return view;
    }

}
