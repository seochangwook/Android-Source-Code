package com.example.apple.sample_app.data;

import android.graphics.Bitmap;

/**
 * Created by apple on 2016. 7. 25..
 */
public class D_News {
    public String headline_str;
    public Bitmap article_image; //네트워크의 데이터는 Bitmap으로 온다.//

    public int like_count; //좋아요 개수//
    public int false_count; //싫어요 개수//

    //후에는 좋아요 점수와 싫어요를 불러올 수 있어야 한다.//

    public String get_headline() {
        return headline_str;
    }

    public int get_like_count() {
        return like_count;
    }

    public int get_false_count() {
        return false_count;
    }
}
