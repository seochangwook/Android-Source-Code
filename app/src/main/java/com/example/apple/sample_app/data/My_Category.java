package com.example.apple.sample_app.data;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 1..
 */
public class My_Category {
    public String category_name;
    public Bitmap category_image;
    public boolean category_lock_option;

    public List<My_Category> my_category_list = new ArrayList<>();

    public String get_category_name() {
        return this.category_name;
    }

    public boolean get_category_lock_option() {
        return this.category_lock_option;
    }
}
