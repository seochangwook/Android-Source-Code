package com.example.apple.sample_app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_News {
    public List<Full_A_News> full_a_newsList = new ArrayList<>(); //첫번째 뉴스기사 목록.//
    int total_news_count = 0; //전체 뉴스의 개수.//

    //후에 추가 가능.//

    public int get_total_news_count() {
        return this.total_news_count;
    }

    public void set_total_news_count(int total_news_count) {
        this.total_news_count = total_news_count;
    }
}
