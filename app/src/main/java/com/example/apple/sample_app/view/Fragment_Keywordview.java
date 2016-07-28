package com.example.apple.sample_app.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.apple.sample_app.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Keywordview extends Fragment {
    Button button;

    public Fragment_Keywordview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_keywordview, container, false);

        button = (Button) view.findViewById(R.id.sample_button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "키워드 방", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
