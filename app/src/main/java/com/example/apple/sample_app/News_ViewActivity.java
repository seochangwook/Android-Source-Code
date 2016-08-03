package com.example.apple.sample_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class News_ViewActivity extends AppCompatActivity {
    private static final String KEY_NEWS_TITLE = "news title";
    private static final String KEY_ARTICLE_HEADLINE = "article headline";
    private static final String KEY_ARTICLE_GOOD_COUNT = "good count";
    private static final String KEY_ARTICLE_BAD_COUNT = "bad count";
    //이미지는 네트워크로 받아오나 현재 요기서는 Dummy Data로 처리//
    private static final String KEY_ARTICLE_IMAGE = "article image";
    private static final String KEY_NEWS_CATEGORY_IMAGE = "news category image";

    String news_title;
    String article_headline;
    String article_good_count;
    String article_bad_count;
    String article_image;
    String news_category_image;

    ImageView news_category_imageview;
    ImageView article_imageview;
    TextView article_headline_text;
    TextView article_content_text;
    Button more_button;
    TextView news_title_text;
    TextView atricle_good_count_text;
    TextView article_bad_count_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_view);

        news_category_imageview = (ImageView) findViewById(R.id.news_image);
        article_imageview = (ImageView) findViewById(R.id.article_image);
        article_headline_text = (TextView) findViewById(R.id.headline_text);
        article_content_text = (TextView) findViewById(R.id.article_content);
        more_button = (Button) findViewById(R.id.more_button);
        news_title_text = (TextView) findViewById(R.id.news_title);
        atricle_good_count_text = (TextView) findViewById(R.id.news_like_count_text);
        article_bad_count_text = (TextView) findViewById(R.id.news_false_count_text);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        initData(intent); //넘어온 값으로 인텐트 초기화.(Dummy data)//

        Log.d("news view tag : ", "뉴스종류 : " + news_title + "/헤드라인 : " + article_headline + "/좋아요 수 : " + article_good_count + "" +
                "/싫어요 수 : " + article_bad_count + "/기사이미지 : " + article_image + "/뉴스 카테고리 이미지 : " + news_category_image);

        settting_news(); //기사의 정보를 초기화.//

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article_content_text.setMovementMethod(new ScrollingMovementMethod()); //스크롤을 생성해준다.//
                article_content_text.setMaxLines(5);
            }
        });

        article_content_text.setEllipsize(TextUtils.TruncateAt.END);
    }

    public void initData(Intent data) {
        /**이미지에 대한 각각의 처리를 한다. 이는 네트워크랑 연동 시 네트워크로 데이터를 가져온다.
         * DB에서의 특정 조건값만 넘긴다.
         */
        news_title = data.getStringExtra(KEY_NEWS_TITLE);
        article_headline = data.getStringExtra(KEY_ARTICLE_HEADLINE);
        article_good_count = data.getStringExtra(KEY_ARTICLE_GOOD_COUNT);
        article_bad_count = data.getStringExtra(KEY_ARTICLE_BAD_COUNT);
        article_image = data.getStringExtra(KEY_ARTICLE_IMAGE);
        news_category_image = data.getStringExtra(KEY_NEWS_CATEGORY_IMAGE);
    }

    public void settting_news() {
        if (news_category_image.equals("1")) {
            news_category_imageview.setImageResource(R.drawable.apple_image_2);
        }

        if (article_image.equals("0/1")) {
            article_imageview.setImageResource(R.drawable.article_sampel_1);
        }

        if (article_image.equals("1/1")) {
            article_imageview.setImageResource(R.drawable.article_sample_2);
        }

        if (article_image.equals("2/1")) {
            article_imageview.setImageResource(R.drawable.article_sample_3);
        }

        if (article_image.equals("3/1")) {
            article_imageview.setImageResource(R.drawable.article_sample_4);
        }

        news_title_text.setText(news_title);
        article_headline_text.setText(article_headline);

        //기사정보는 네트워크로 가져온다.//
        article_content_text.setText("서창욱은 경기도 수원시 장안구 정자동 동신아파트에 살고 있습니다. 좋아하는 분야는" +
                "모바일 개발 IOT입니다 현재 T아카데미에서 프로젝트 모바일 프로젝트 진행 중에 있고 앞으로 3개월간 인턴도 할 예정입니다" +
                "저의 목표는 IoT와 모바일의 생태계에서 멋진 개발자가 되는것입니다.안녕하세요안녕하세요 팀프로젝트 팀프로젝트 화이팅 화이팅" +
                "뉴스잉 뉴스잉 뉴스잉 ");

        atricle_good_count_text.setText(article_good_count);
        article_bad_count_text.setText(article_bad_count);
    }
}
