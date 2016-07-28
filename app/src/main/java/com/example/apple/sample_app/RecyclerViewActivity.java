package com.example.apple.sample_app;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.apple.sample_app.data.A_News;
import com.example.apple.sample_app.data.B_News;
import com.example.apple.sample_app.data.C_News;
import com.example.apple.sample_app.data.News;
import com.example.apple.sample_app.view.HeaderAndFooterViewUtil;
import com.example.apple.sample_app.widget.MultiListAdapter;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRecyclerViewOnScrollListener;

public class RecyclerViewActivity extends AppCompatActivity {
    FamiliarRecyclerView listview; //기존 ListView보다 더 활용성이 좋고 실용적인 RecyclerView를 사용.//
    MultiListAdapter mAdapter;
    SwipeRefreshLayout mSwipeRefresh;

    private boolean isVertical = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);

        isVertical = getIntent().getBooleanExtra("isVertical", true);


        listview = (FamiliarRecyclerView) findViewById(R.id.rv_list);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_wrapper);

        mAdapter = new MultiListAdapter(this); //어댑터 정의.//

        listview.setAdapter(mAdapter); //어댑터 장착.//

        listview.setItemAnimator(new DefaultItemAnimator());

        //스크롤이 되었을 경우 이벤트 처리//
        listview.addOnScrollListener(new FamiliarRecyclerViewOnScrollListener(listview.getLayoutManager()) {
            @Override
            public void onScrolledToTop() {
                Log.i("EVENT : ", "onScrolledTop");

                Toast.makeText(RecyclerViewActivity.this, "가장 상단입니다", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onScrolledToBottom() {
                Log.i("EVENT : ", "scrolled to bottom");

                Toast.makeText(RecyclerViewActivity.this, "가장 하단입니다", Toast.LENGTH_SHORT).show();
            }
        });

        //리스트 아이템 클릭 이벤트.//
        listview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Log.i("wg", "onItemClick = " + familiarRecyclerView + " _ " + view + " _ " + position);
                Toast.makeText(RecyclerViewActivity.this, "onItemClick = " + position, Toast.LENGTH_SHORT).show();
            }
        });

        //리스트 아이템 롱클릭 이벤트.//
        listview.setOnItemLongClickListener(new FamiliarRecyclerView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                Log.i("wg", "onItemLongClick = " + familiarRecyclerView + " _ " + view + " _ " + position);
                Toast.makeText(RecyclerViewActivity.this, "onItemLongClick = " + position, Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        mSwipeRefresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // DO REFRESH THEN FINISH REFRESHING
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        mSwipeRefresh.setRefreshing(false);

                        Log.i("refresh list : ", "item refresh");
                    }
                }, 1000);
            }
        });

        initData(); //데이터 셋 초기화//
    }

    //액션바를 정의//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_layout, menu); //xml로 작성된 메뉴를 팽창//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //샘플 헤더뷰 풋터뷰 생성//

        switch (item.getItemId()) {
            case R.id.add_headerview: {
                //Toast.makeText(RecyclerViewActivity.this, "add headerview", Toast.LENGTH_SHORT).show();

                listview.addHeaderView(HeaderAndFooterViewUtil.getHeadView(this, isVertical, 0xFFFF5000, "Head View 1"));

                return true;
            }

            case R.id.del_headerview: {
                //Toast.makeText(RecyclerViewActivity.this, "del headerview", Toast.LENGTH_SHORT).show();

                listview.removeHeaderView(HeaderAndFooterViewUtil.getHeadView(this, isVertical, 0xFFFF5000, "Head View 1"));

                return true;
            }

            case R.id.add_footerview: {
                //Toast.makeText(RecyclerViewActivity.this, "add footerview", Toast.LENGTH_SHORT).show();

                listview.addFooterView(HeaderAndFooterViewUtil.getFooterView(this, isVertical, 0xFF778899, "Foot_View 1"));

                return true;
            }

            case R.id.del_footerview: {
                //Toast.makeText(RecyclerViewActivity.this, "del footerview", Toast.LENGTH_SHORT).show();

                listview.removeFooterView(HeaderAndFooterViewUtil.getFooterView(this, isVertical, 0xFF778899, "Foot_View 1"));

                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void initData() {
        //해당 데이터 초기화 부분은 네트워크(JSON)로 파싱 후 가져온다.//
        //헤더와 각 뉴스에 맞게 데이터를 초기화.//

        /*Test data*/
        News news = new News();

        news.news_list = "조선일보 / 동아일보 / 매일경제";
        news.total_news_count = 15;
        news.get_news_date = "2016/07/24";
        news.info_icon = R.drawable.messenger_bubble_large_blue;

        //A_News 1 data//
        A_News a_news = new A_News();

        a_news.headline_str = "계약...김정주 지시없이 가능했겠나";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sampel_1);

        news.a_news.add(a_news);

        //A_News 2 data//
        a_news = new A_News();

        a_news.headline_str = "우리도 '우병우 청문회' 막기 어렵다.";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_2);

        news.a_news.add(a_news);

        //A_News 3 data//
        a_news = new A_News();

        a_news.headline_str = "대기업에 번지는 반바지 출근";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_3);

        news.a_news.add(a_news);

        //A_News 4 data//
        a_news = new A_News();

        a_news.headline_str = "주말 낮 최고기온 33도";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_4);

        news.a_news.add(a_news);

        //A_News 5 data//
        a_news = new A_News();

        a_news.headline_str = "이중섭 그림...그를 위한 서울미술관";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_5);

        news.a_news.add(a_news);

        //B_News 1 data//
        B_News b_news = new B_News();

        b_news.headline_str = "제가 대표된다면 바뀐 새누리 모습 보여주는 셈";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_6);

        news.b_news.add(b_news);

        //B_News 2 data//
        b_news = new B_News();

        b_news.headline_str = "놀아주고 돈 벌고...'놀이시터'를 아시나요";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_7);

        news.b_news.add(b_news);

        //B_News 3 data//
        b_news = new B_News();

        b_news.headline_str = "햄버거가 뭐길래...밤샘 줄서기";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_8);

        news.b_news.add(b_news);

        //B_News 4 data//
        b_news = new B_News();

        b_news.headline_str = "SK이노베이션 '깜짝실적'...매출 20%증가";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_9);

        news.b_news.add(b_news);

        //B_News 5 data//
        b_news = new B_News();

        b_news.headline_str = "춤추는 볼트, 충전 완료";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_10);

        news.b_news.add(b_news);

        //C_News 1 data//
        C_News c_news = new C_News();

        c_news.headline_str = "눈도 마음도 홀린 이방카";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_11);

        news.c_news.add(c_news);

        //C_News 2 data//
        c_news = new C_News();

        c_news.headline_str = "11년만에 건설사업 없는 추경...";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_12);

        news.c_news.add(c_news);

        //C_News 3 data//
        c_news = new C_News();

        c_news.headline_str = "트럼프,'성완종' 딛고 재기하나";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_13);

        news.c_news.add(c_news);

        //C_News 4 data//
        c_news = new C_News();

        c_news.headline_str = "지진괴담으로 번진 가스냄새";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_14);

        news.c_news.add(c_news);

        //C_News 5 data//
        c_news = new C_News();

        c_news.headline_str = "대우조선의 파업은 회사 문 닫게 해달라는 것";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_15);

        news.c_news.add(c_news);

        //최종적으로 만들어진 데이터를 어댑터에 장착//
        mAdapter.setNews(news);
    }
}
