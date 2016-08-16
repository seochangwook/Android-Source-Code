package com.example.apple.sample_app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.sample_app.widget.MenuWidget.BottomMenu;

import java.util.ArrayList;

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

    BottomMenu mBottomMenu;

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
        mBottomMenu = (BottomMenu) findViewById(R.id.bottom_menu_2);

        setupBottomMenuTabs(); //하단메뉴를 생성.//

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        initData(intent); //넘어온 값으로 인텐트 초기화.(Dummy data)//

        Log.d("news view tag : ", "뉴스종류 : " + news_title + "/헤드라인 : " + article_headline + "/좋아요 수 : " + article_good_count + "" +
                "/싫어요 수 : " + article_bad_count + "/기사이미지 : " + article_image + "/뉴스 카테고리 이미지 : " + news_category_image);

        settting_news(); //기사의 정보를 초기화.//

        more_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                article_content_text.setMovementMethod(new ScrollingMovementMethod()); //스크롤을 생성해준다.//
                article_content_text.setMaxLines(20);
                more_button.setVisibility(View.GONE);
            }
        });

        article_content_text.setEllipsize(TextUtils.TruncateAt.END);
    }

    /**
     * Bottom Menu 관련
     **/
    private void setupBottomMenuTabs() {
        ArrayList<BottomMenu.BottomMenuItem> items = new ArrayList<>();
        BottomMenu.BottomMenuItem news = new BottomMenu.BottomMenuItem(R.id.bottom_bar_five, R.drawable.ic_account_balance_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem tw = new BottomMenu.BottomMenuItem(R.id.bottom_bar_six, R.drawable.ic_face_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem listing = new BottomMenu.BottomMenuItem(R.id.bottom_bar_seven, R.drawable.ic_subscriptions_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);
        BottomMenu.BottomMenuItem tv = new BottomMenu.BottomMenuItem(R.id.bottom_bar_eight, R.drawable.ic_whatshot_black_24dp, R.color.bottom_bar_unselected, R.color.bottom_bar_selected);

        items.add(news);
        items.add(tw);
        items.add(listing);
        items.add(tv);

        mBottomMenu.addItems(items);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupBottomMenuClickListener();
    }

    private void setupBottomMenuClickListener() {
        mBottomMenu.setBottomMenuClickListener(new BottomMenu.BottomMenuClickListener() {
            @Override
            public void onItemSelected(int position, int id, boolean triggeredOnRotation) {
                // Do something when item is selected
                if (position == 1) {
                    Toast.makeText(News_ViewActivity.this, "메뉴 2 클릭", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemReSelected(int position, int id) {
                // Do something when item is re-selected
                if (position == 0) {
                    Toast.makeText(News_ViewActivity.this, "메뉴 1 클릭", Toast.LENGTH_SHORT).show();
                }
            }
        });
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
        article_content_text.setText("삼성전자가 이탈리아 자동차업체 피아트크라이슬러의 부품 사업 부문을 인수하려고 협상을 진행하고 있습니다.\n" +
                "\n" +
                "지난해 조직 개편에서 자동차 전담 부서를 만들었던 이재용 부회장의 스마트카 사업이 탄력을 받을 수 있을지 주목됩니다.\n" +
                "\n" +
                "이정미 기자입니다.\n" +
                "\n" +
                "기자\n" +
                "\n" +
                "삼성전자가 인수 협상을 벌이고 있는 업체는 이탈리아 피아트크라이슬러의 부품 사업 부문, 마그네티 마렐리입니다.\n" +
                "\n" +
                "1919년 설립된 회사로 지난해 약 9조 천4백억 원의 매출을 올렸습니다.\n" +
                "\n" +
                "19개 국가에 직원이 4만 명이 넘고 12개 연구개발센터도 보유하고 있습니다.\n" +
                "\n" +
                "블룸버그 통신은 삼성전자가 마그네티 마렐리를 인수하는 협상을 마무리 지을 계획이라며 차량 조명과 무선 인터넷 기술에 특히 관심을 보였다고 보도했습니다.\n" +
                "\n" +
                "삼성도 인수 협상 자체를 부인하지 않았습니다.\n" +
                "\n" +
                "인수설이 보도되자 이탈리아 증시에서 피아트 크라이슬러의 주가는 장 초반 폭등했습니다.\n" +
                "\n" +
                "인수 예상가는 3조 4천억 원 이상으로 협상이 성사된다면 해외 기업 인수로는 최대 규모입니다.\n" +
                "\n" +
                "삼성전자 이재용 부회장은 2012년부터 피아트 크라이슬러 그룹의 사외이사를 맡으면서 우호적인 관계를 유지해왔습니다.\n" +
                "\n" +
                "지난해에는 자신이 주도한 첫 조직 개편에서 자동차 전자 부품 부서를 새로 만들며 삼성자동차를 매각한 이후 15년 만에 자동차 사업에 발동을 걸었습니다.\n" +
                "\n" +
                "[이준호 / 삼성전자 커뮤니케이션팀 부장(지난해 12월) : 초기에 인포테인먼트와 자율 주행 중심으로 역량을 집중하고 향후에도 계열사 간 협력을 통해 시너지를 높일 계획입니다.]\n" +
                "\n" +
                "반도체나 모바일 분야에서 앞선 기술을 보유하고 있기 때문에 구글이나 애플처럼 스마트카 시장에 진출할 것이라는 관측도 나왔습니다.\n" +
                "\n" +
                "하지만 삼성전자는 자동차 부품 사업만 하고 있다며 완성차 시장에는 뛰어들 계획이 없다고 선을 그었습니다.");

        atricle_good_count_text.setText(article_good_count);
        article_bad_count_text.setText(article_bad_count);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //메뉴의 생성은 MenuInflater이용//
        getMenuInflater().inflate(R.menu.friend_menu, menu); //메뉴 생성(xml)//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  //메뉴의 선택.//
    {
        switch (item.getItemId()) {
            case R.id.friend_menu_1: {
                Toast.makeText(News_ViewActivity.this, "기사 스크랩", Toast.LENGTH_SHORT).show();

                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
