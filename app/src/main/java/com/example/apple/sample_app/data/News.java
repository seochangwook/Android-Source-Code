package com.example.apple.sample_app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 7. 24..
 */
public class News {
    public String news_list;
    public int total_news_count; //전체 뉴스의 개수//
    public String get_news_date; //뉴스데이터를 가져온 시간//
    public int info_icon; //아이콘//

    //각 뉴스배열 정의//
    public List<A_News> a_news = new ArrayList<>();
    public List<B_News> b_news = new ArrayList<>();
    public List<C_News> c_news = new ArrayList<>();
    public List<D_News> d_news = new ArrayList<>();

    public String get_news_list() {
        return news_list;
    }

    public String get_news_date() {
        return get_news_date;
    }

    public int get_total_news_count() {
        return total_news_count;
    }

    public void set_total_news_count(int news_count) {
        this.total_news_count = news_count;
    }

    public void remove_all() //전체 배열의 정보를 지우는 경우.//
    {
        a_news.removeAll(a_news);
        b_news.removeAll(b_news);
        c_news.removeAll(c_news);
        d_news.removeAll(d_news);
    }
}
