package com.example.apple.sample_app.dialog;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.widget.Adapter.MyGroupAdapter;

public class Keyword_dialog_activity extends Activity
{
    private static final int RC_KEYWORD_CODE = 100; //정상응답을 받을 경우//
    private static final String RC_KEYWORD_KEY = "keyword";
    ExpandableListView expandablelistview;
    MyGroupAdapter mAdapter;
    String group_name[];
    String child_name[];
    int child_image_id[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_keyword_dialog_activity);

        expandablelistview = (ExpandableListView)findViewById(R.id.expandableListView);

        mAdapter = new MyGroupAdapter(this);
        expandablelistview.setAdapter(mAdapter);

        //리스트뷰 이벤트 처리.//
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(Keyword_dialog_activity.this, "원하는 카테고리를 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });

        //자식뷰가 선택되었을 경우.//
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(Keyword_dialog_activity.this, "선택된 카테고리 : " + child_name[childPosition], Toast.LENGTH_SHORT).show();

                String keyword = child_name[childPosition].toString();

                Log.d("selct keyword : ", keyword);

                Intent intent = new Intent();

                intent.putExtra(RC_KEYWORD_KEY, keyword);

                setResult(RESULT_OK, intent);

                finish();

                return false;
            }
        });

        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(Keyword_dialog_activity.this, "카테고리 목록 닫기", Toast.LENGTH_SHORT).show();
            }
        });

        set_ExpanList_Data();
    }

    public void set_ExpanList_Data() {
        //데이터 초기화.(네트워크로 부터 데이터를 로드한다.)//
        group_name = new String[]{"Category"};
        child_name = new String[]{"사회이슈", "인물", "정치"};
        child_image_id = new int[]{R.drawable.apple_image, R.drawable.apple_image_2, R.drawable.apple_image_3};

        //이중for문으로 그룹 당 자식을 생성.//
        for (int group_index = 0; group_index < group_name.length; group_index++) {
            for (int child_index = 0; child_index < child_name.length; child_index++) {
                String groupname = group_name[group_index];
                String childname = child_name[child_index];
                int child_image = child_image_id[child_index];

                mAdapter.set_List_Data(groupname, childname, child_image);
            }
        }
    }
}
