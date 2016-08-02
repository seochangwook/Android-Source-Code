package com.example.apple.sample_app;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import co.hkm.soltag.TagContainerLayout;
import co.hkm.soltag.TagView;

public class Tag_Sample_Activity extends AppCompatActivity {
    TagContainerLayout mTagContainerLayout; //태그를 담고 있는 레이아웃 위젯 정의.//

    EditText input_edit;
    Button tag_add_button;
    Button read_tag_button;

    List<String> tag_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_sample_layout);

        mTagContainerLayout = (TagContainerLayout) findViewById(R.id.tag_container_layout);
        input_edit = (EditText) findViewById(R.id.input_tag_edit);
        tag_add_button = (Button) findViewById(R.id.add_tag_button);
        read_tag_button = (Button) findViewById(R.id.read_tag_button);

        read_tag_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //태그 데이터 불러오기.//
                tag_list = mTagContainerLayout.getTags();

                for (int i = 0; i < tag_list.size(); i++) {
                    Toast.makeText(Tag_Sample_Activity.this, "tag[" + i + "] name : " + tag_list.get(i), Toast.LENGTH_SHORT).show();
                }
            }
        });

        tag_add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input_tag_name = input_edit.getText().toString();

                mTagContainerLayout.addTag("#" + input_tag_name); //태그 추가.//
            }
        });

        //태그 이벤트 처리.//
        mTagContainerLayout.setOnTagClickListener(new TagView.OnTagClickListener() {
            @Override
            public void onTagClick(int position, String text) {
                //클릭된 태그의 값을 가져온다.//
                String tag_name = mTagContainerLayout.getTagText(position);

                Toast.makeText(Tag_Sample_Activity.this, "Tag name : " + tag_name, Toast.LENGTH_SHORT).show();

                Log.d("tag name : ", tag_name);
            }

            @Override
            public void onTagLongClick(int position, String text) {
                //클릭된 태그의 값을 가져온다.//
                String tag_name = mTagContainerLayout.getTagText(position);

                Log.d("delete tag name : ", tag_name);

                mTagContainerLayout.removeTag(position); //태그 제거.//
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
