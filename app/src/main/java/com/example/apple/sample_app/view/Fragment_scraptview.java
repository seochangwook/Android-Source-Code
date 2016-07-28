package com.example.apple.sample_app.view;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apple.sample_app.R;
import com.michaldrabik.tapbarmenulib.TapBarMenu;


/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_scraptview extends Fragment {
    TapBarMenu tapBarMenu; //메뉴생성//
    /**
     * 탭바에 있는 아이템
     **/
    ImageView item_1;
    ImageView item_2;
    ImageView item_3;
    ImageView item_4;

    public Fragment_scraptview() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_scraptview, container, false);

        tapBarMenu = (TapBarMenu) view.findViewById(R.id.tapBarMenu_2);

        /** TabBar에서 쓰일 아이템 리소스 **/
        item_1 = (ImageView) view.findViewById(R.id.item5);
        item_2 = (ImageView) view.findViewById(R.id.item6);
        item_3 = (ImageView) view.findViewById(R.id.item7);
        item_4 = (ImageView) view.findViewById(R.id.item8);

        tapBarMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tapBarMenu.toggle();
            }
        });

        /** TapBar토글 아이템 메뉴처리 **/
        item_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "item 2-1 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "item 2-2 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "item 2-3 click", Toast.LENGTH_SHORT).show();
            }
        });

        item_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "item 2-4 click", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
