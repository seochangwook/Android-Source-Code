package com.example.apple.sample_app.view.view_list;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 16..
 */
public class GroupItem {
    public String group_name;

    //그룹은 각 그룹에 따른 자식을 가질 수 있다.//
    public List<ChildItem> children = new ArrayList<>();
}
