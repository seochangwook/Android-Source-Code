package com.example.apple.sample_app.widget.Adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.view.view_list.ChildItem;
import com.example.apple.sample_app.view.view_list.GroupItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 16..
 */
public class MyGroupAdapter extends BaseExpandableListAdapter {
    Context context;
    //그룹에 대한 리스트 정의.//
    List<GroupItem> items = new ArrayList<>();

    public MyGroupAdapter(Context context) {
        this.context = context;
    }

    public void set_List_Data(String groupName, String childName, int image_id) {
        GroupItem group = null;

        for (GroupItem g : items) {
            if (g.group_name.equals(groupName)) {
                group = g;

                break;
            }
        }

        if (group == null) //만약 그룹이 할당이 안되었으면.//
        {
            group = new GroupItem(); //그룹을 만들어준다.//
            group.group_name = groupName;

            items.add(group); //그룹을 추가.//
        }

        //그룹 당 자식의 객체를 생성.//
        if (!TextUtils.isEmpty(childName)) {
            ChildItem child = new ChildItem(); //자식을 생성.//

            child.child_name = childName;
            child.image_id = image_id;

            group.children.add(child); //해당 그룹에 자식을 등록.//
        }
    }

    @Override
    public int getGroupCount()  //그룹의 개수를 반환.//
    {
        return items.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) //그룹 당 자식의 개수를 반환.//
    {
        return items.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) //그룹 하나를 얻어온다.//
    {
        return items.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) //그룹 당 자식을 얻어온다.//
    {
        return items.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    //그룹뷰에 대한 설정//
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View groupItem_view;

        TextView group_name_text = null;
        Button more_category_button = null;

        if (convertView == null) {
            groupItem_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);

            group_name_text = (TextView) groupItem_view.findViewById(R.id.group_name_text);
            more_category_button = (Button) groupItem_view.findViewById(R.id.more_category_button);
        } else {
            groupItem_view = convertView;

            group_name_text = (TextView) groupItem_view.findViewById(R.id.group_name_text);
            more_category_button = (Button) groupItem_view.findViewById(R.id.more_category_button);
        }

        group_name_text.setText(items.get(groupPosition).group_name);

        return group_name_text;
    }

    //자식뷰에 대한 설정.//
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        View child_item_view;

        ImageView child_item_image = null;
        TextView child_item_name = null;

        if (convertView == null) {
            child_item_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_item, parent, false);

            child_item_image = (ImageView) child_item_view.findViewById(R.id.child_imageview);
            child_item_name = (TextView) child_item_view.findViewById(R.id.child_name_text);
        } else {
            child_item_view = convertView;

            child_item_image = (ImageView) child_item_view.findViewById(R.id.child_imageview);
            child_item_name = (TextView) child_item_view.findViewById(R.id.child_name_text);
        }

        child_item_image.setImageResource(items.get(groupPosition).children.get(childPosition).image_id);
        child_item_name.setText(items.get(groupPosition).children.get(childPosition).child_name);

        return child_item_view;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
