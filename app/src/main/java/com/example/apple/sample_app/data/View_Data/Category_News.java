package com.example.apple.sample_app.data.View_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 7..
 */
public class Category_News {
    public String category_name;
    public int category_code;
    public String category_imageUrl;

    public List<Category_News> category_news_array = new ArrayList<>();

    public String get_category_name() {
        return this.category_name;
    }

    public void set_category_name(String category_name) {
        this.category_name = category_name;
    }

    public int get_category_code() {
        return this.category_code;
    }

    public void set_category_code(int category_code) {
        this.category_code = category_code;
    }

    public String get_category_imageUrl() {
        return this.category_imageUrl;
    }

    public void set_category_imaegUrl(String category_imageUrl) {
        this.category_imageUrl = category_imageUrl;
    }
}
