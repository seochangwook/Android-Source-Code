package com.example.apple.sample_app.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_scraptview extends Fragment {


    public Fragment_scraptview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scraptview, container, false);

        return view;
    }

}
