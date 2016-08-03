package com.example.apple.sample_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.apple.sample_app.data.A_News;
import com.example.apple.sample_app.data.B_News;
import com.example.apple.sample_app.data.C_News;
import com.example.apple.sample_app.data.D_News;
import com.example.apple.sample_app.data.News;
import com.example.apple.sample_app.view.LoadMoreView;
import com.example.apple.sample_app.widget.MultiListAdapter;
import com.example.apple.sample_app.widget.Scrapt_MultiListAdapter;

import java.io.FileNotFoundException;
import java.io.IOException;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

/**
 * Created by apple on 2016. 7. 25..
 */
public class RecyclerViewActivity_Refresh extends AppCompatActivity {
    final int REQ_CODE_SELECT_IMAGE = 100;
    MultiListAdapter mAdapter; //어댑터//
    Scrapt_MultiListAdapter smAdapter; //스크랩 가능한 어댑터.//
    View header_view; //헤더뷰.//
    View footer_view; //푸터뷰.//
    //헤더뷰와 푸터뷰에서 쓰일 위젯 정의//
    EditText input_headline_str_edit;
    Button get_image_button;
    ImageView get_imageview;
    RadioButton a_news_button;
    RadioButton b_news_button;
    RadioButton c_news_button;
    RadioButton d_news_button;
    RadioGroup group;
    Button enroll_button;
    FloatingActionButton info_1_button;
    int select_number; //0->조선일보, 1->동아일보, 2->매일경제, 3->한국일보//
    Bitmap image_bitmap; //이미지를 저장할 변수(포맷은 비트맵)//
    News news;
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//
    private boolean isVertical = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_refresh);

        header_view = getLayoutInflater().inflate(R.layout.header_view, null, false);
        footer_view = getLayoutInflater().inflate(R.layout.footer_view, null, false);

        isVertical = getIntent().getBooleanExtra("isVertical", true);

        recyclerview_refresh = (FamiliarRefreshRecyclerView) findViewById(R.id.rv_list);

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(this)); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        input_headline_str_edit = (EditText) header_view.findViewById(R.id.input_headline);
        get_image_button = (Button) header_view.findViewById(R.id.get_image_button);
        get_imageview = (ImageView) header_view.findViewById(R.id.get_imageview);
        a_news_button = (RadioButton) header_view.findViewById(R.id.select_anews_button);
        b_news_button = (RadioButton) header_view.findViewById(R.id.select_bnews_button);
        c_news_button = (RadioButton) header_view.findViewById(R.id.select_cnews_button);
        d_news_button = (RadioButton) header_view.findViewById(R.id.select_dnews_button);
        enroll_button = (Button) header_view.findViewById(R.id.enroll_button);
        group = (RadioGroup) header_view.findViewById(R.id.radiogroup);
        info_1_button = (FloatingActionButton) findViewById(R.id.info_button_1);

        news = new News();

        mAdapter = new MultiListAdapter(this); //어댑터 정의.//

        /** ActionMenu 설정 **/

        //새로고침되어서 다시 리사이클뷰와 연동될 수 있도록 한다.//
        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        //리스트의 경계선을 데코레이션//
        /*Paint paint = new Paint();
        paint.setStrokeWidth(5);
        paint.setColor(Color.BLUE);
        paint.setAntiAlias(true);
        paint.setPathEffect(new DashPathEffect(new float[]{45.0f, 45.0f}, 2));

        recyclerview.addItemDecoration(new HorizontalDividerItemDecoration.Builder(this).paint(paint).build());*/

        /** Data refresh **/
        //사용자가 위에서 새로고침 할 경우//
        recyclerview_refresh.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        recyclerview_refresh.pullRefreshComplete();

                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //사용자가 아래쪽에서 새로고침//
        recyclerview_refresh.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        recyclerview_refresh.loadMoreComplete();

                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        recyclerview.setAdapter(mAdapter); //리사이클뷰에 어댑터 장착//

        /******************** HeaderView 작업 ********************/
        //선태된 이미지를 가져오기//
        get_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);

            }
        });

        //라디오 버튼 이벤트 처리//
        group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkId) {
                switch (checkId) {
                    case R.id.select_anews_button: {
                        select_number = 0;

                        break;
                    }

                    case R.id.select_bnews_button: {
                        select_number = 1;

                        break;
                    }

                    case R.id.select_cnews_button: {
                        select_number = 2;

                        break;
                    }

                    case R.id.select_dnews_button: {
                        select_number = 3;

                        break;
                    }
                }
            }
        });

        /** 등록과정(Enroll) **/
        enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String headline_str = input_headline_str_edit.getText().toString();

                enroll_news(headline_str, image_bitmap, select_number);
            }
        });

        info_1_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view,
                        "원하시는 기사에 버튼을 누르면 스크랩 목록에 추가됩니다.!!" + "\n",
                        Snackbar.LENGTH_SHORT).setAction("Action", null).show();

                /** 현재의 어댑터를 스크랩가능한 어댑터로 변경 **/
                recyclerview.setAdapter(smAdapter); //리사이클뷰에 스크랩 어댑터 장착//

                smAdapter = null;

                smAdapter = new Scrapt_MultiListAdapter(RecyclerViewActivity_Refresh.this);

                info_1_button.setVisibility(View.GONE);

                news.remove_all();

                scrapted_initData(); //데이터 셋 초기화//

                /** 현재 Menu의 모양을 스크랩 전용으로 변경 **/
                startSupportActionMode(new ActionMode.Callback() {
                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        //Create단계에서 inflate시킨다.//
                        getMenuInflater().inflate(R.menu.scrapt_menu_mode, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.cancel_scratp: //스크랩 취소 버튼.//
                            {
                                mAdapter = null; //다시 어댑터를 장착하기 위해서 우선 null로 초기화.//
                                mAdapter = new MultiListAdapter(RecyclerViewActivity_Refresh.this); //어댑터 정의.//
                                recyclerview.setAdapter(mAdapter); //리사이클뷰에 기존 어댑터 장착//

                                info_1_button.setVisibility(View.VISIBLE);

                                //데이터를 다 지운다.//
                                news.remove_all();
                                re_initData(); //데이터 셋 초기화//

                                mode.finish(); //종료//

                                break;
                            }

                            case R.id.enroll_scrapt: //스크랩 버튼을 눌렀을 경우.//
                            {
                                /** 선택된 정보를 저장 **/
                                enroll_my_news_data();

                                mAdapter = null; //다시 어댑터를 장착하기 위해서 우선 null로 초기화.//
                                mAdapter = new MultiListAdapter(RecyclerViewActivity_Refresh.this); //어댑터 정의.//
                                recyclerview.setAdapter(mAdapter); //리사이클뷰에 기존 어댑터 장착//

                                info_1_button.setVisibility(View.VISIBLE);

                                //데이터를 다 지운다.//
                                news.remove_all();
                                re_initData(); //데이터 셋 초기화//

                                mode.finish(); //종료//

                                break;
                            }
                        }

                        return true;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {
                        mAdapter = null; //다시 어댑터를 장착하기 위해서 우선 null로 초기화.//
                        mAdapter = new MultiListAdapter(RecyclerViewActivity_Refresh.this); //어댑터 정의.//
                        recyclerview.setAdapter(mAdapter); //리사이클뷰에 기존 어댑터 장착//

                        info_1_button.setVisibility(View.VISIBLE);

                        //데이터를 다 지운다.//
                        news.remove_all();
                        re_initData(); //데이터 셋 초기화//
                    }
                });
            }
        });

        /** Initialize Data **/
        initData(); //데이터 셋 초기화//
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) //이미지를 가져오는 요청에 대해서 처리//
        {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    get_imageview.setImageBitmap(image_bitmap);


                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //액션바를 정의//
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.actionbar_layout, menu); //xml로 작성된 메뉴를 팽창//

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //헤더뷰와 푸터뷰의 뷰 레이아웃 삽입.//
        switch (item.getItemId()) {
            case R.id.add_headerview: {
                //Toast.makeText(RecyclerViewActivity.this, "add headerview", Toast.LENGTH_SHORT).show();

                recyclerview.addHeaderView(header_view, true);

                mAdapter.notifyDataSetChanged();

                return true;
            }

            case R.id.del_headerview: {
                //Toast.makeText(RecyclerViewActivity.this, "del headerview", Toast.LENGTH_SHORT).show();

                recyclerview.removeHeaderView(header_view);

                mAdapter.notifyDataSetChanged();

                return true;
            }

            case R.id.add_footerview: {
                //Toast.makeText(RecyclerViewActivity.this, "add footerview", Toast.LENGTH_SHORT).show();

                recyclerview.addFooterView(footer_view, true);

                mAdapter.notifyDataSetChanged();

                return true;
            }

            case R.id.del_footerview: {
                //Toast.makeText(RecyclerViewActivity.this, "del footerview", Toast.LENGTH_SHORT).show();

                recyclerview.removeFooterView(footer_view);

                mAdapter.notifyDataSetChanged();

                return true;
            }

            case R.id.next_page: {
                Intent intent = new Intent(RecyclerViewActivity_Refresh.this, TabActivity.class);

                startActivity(intent);
            }
        }

        return super.onOptionsItemSelected(item);
    }

    public void enroll_news(String enroll_headline_str, Bitmap bitmap, int select_news_item) {
        /** 데이터의 초기화는 데이터베이스(SQLite)와 네트워크(파싱) 등 다양한 방법으로 가능한다. **/
        /**다른 뉴스객체는 이미지 설정이 비트맵이 아니라 추가가 불가. **/

        //각 해당 선택된 뉴스에 따라 분기.//
        if (select_news_item == 0) {
            //A뉴스 객체 생성 후 저장.//
        } else if (select_news_item == 1) {
            //B뉴스 객체 생성 후 저장.//
        } else if (select_news_item == 2) {
            //C뉴스 객체 생성 후 저장.//
        } else if (select_news_item == 3) {
            //D뉴스 객체 생성 후 저장.//
            D_News d_news = new D_News();

            /** 추후 좋아요개수나 싫어요 개수는 서버에 의해서 데이터가 갱신된다. **/
            int default_like_count = 0;
            int default_false_count = 0;

            //D_news의 객체를 만들어준다.//
            d_news.headline_str = enroll_headline_str;
            d_news.article_image = bitmap;
            d_news.like_count = default_like_count;
            d_news.false_count = default_false_count;

            //서버나 데이터베이스에도 결과가 포함된다.//
            news.d_news.add(d_news);

            mAdapter.setNews(news);
            mAdapter.setNews_count(1); //1나씩 계속 추가되는 것이므로.//

            //좋아요수와 싫어요수를 0으로 초기화//

            Toast.makeText(RecyclerViewActivity_Refresh.this, "기사가 추가되었습니다", Toast.LENGTH_SHORT).show();
        }
    }

    /***************
     * Data Setting
     ****************/

    public void initData() {
        //해당 데이터 초기화 부분은 네트워크(JSON, xml)로 파싱 후 가져온다.//
        //헤더(사용자가 선택한 키워드)와 각 뉴스에 맞게 데이터를 초기화.//
        //뉴스의 번호를 넣어야 한다면 사이즈 동기화를 위해서 리스트의 순서를 넣는다.//

        /*Test data*/
        int news_count = 0;

        news.news_list = "조선일보 / 동아일보 / 매일경제 / 한국일보";
        news.get_news_date = "2016/07/24";
        news.info_icon = R.drawable.messenger_bubble_large_blue;

        //A_News 1 data//
        A_News a_news = new A_News();

        a_news.headline_str = "계약...김정주 지시없이 가능했겠나";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sampel_1);

        news.a_news.add(a_news);
        news_count++;

        //A_News 2 data//
        a_news = new A_News();

        a_news.headline_str = "우리도 '우병우 청문회' 막기 어렵다.";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_2);

        news.a_news.add(a_news);
        news_count++;

        //A_News 3 data//
        a_news = new A_News();

        a_news.headline_str = "대기업에 번지는 반바지 출근";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_3);

        news.a_news.add(a_news);
        news_count++;

        //A_News 4 data//
        a_news = new A_News();

        a_news.headline_str = "주말 낮 최고기온 33도";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_4);

        news.a_news.add(a_news);
        news_count++;

        //A_News 5 data//
        a_news = new A_News();

        a_news.headline_str = "이중섭 그림...그를 위한 서울미술관";
        a_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_5);

        news.a_news.add(a_news);
        news_count++;

        //B_News 1 data//
        B_News b_news = new B_News();

        b_news.headline_str = "제가 대표된다면 바뀐 새누리 모습 보여주는 셈";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_6);

        news.b_news.add(b_news);
        news_count++;

        //B_News 2 data//
        b_news = new B_News();

        b_news.headline_str = "놀아주고 돈 벌고...'놀이시터'를 아시나요";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_7);

        news.b_news.add(b_news);
        news_count++;

        //B_News 3 data//
        b_news = new B_News();

        b_news.headline_str = "햄버거가 뭐길래...밤샘 줄서기";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_8);

        news.b_news.add(b_news);
        news_count++;

        //B_News 4 data//
        b_news = new B_News();

        b_news.headline_str = "SK이노베이션 '깜짝실적'...매출 20%증가";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_9);

        news.b_news.add(b_news);
        news_count++;

        //B_News 5 data//
        b_news = new B_News();

        b_news.headline_str = "춤추는 볼트, 충전 완료";
        b_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_10);

        news.b_news.add(b_news);
        news_count++;

        //C_News 1 data//
        C_News c_news = new C_News();

        c_news.headline_str = "눈도 마음도 홀린 이방카";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_11);

        news.c_news.add(c_news);
        news_count++;

        //C_News 2 data//
        c_news = new C_News();

        c_news.headline_str = "11년만에 건설사업 없는 추경...";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_12);

        news.c_news.add(c_news);
        news_count++;

        //C_News 3 data//
        c_news = new C_News();

        c_news.headline_str = "트럼프,'성완종' 딛고 재기하나";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_13);

        news.c_news.add(c_news);
        news_count++;

        //C_News 4 data//
        c_news = new C_News();

        c_news.headline_str = "지진괴담으로 번진 가스냄새";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_14);

        news.c_news.add(c_news);
        news_count++;

        //C_News 5 data//
        c_news = new C_News();

        c_news.headline_str = "대우조선의 파업은 회사 문 닫게 해달라는 것";
        c_news.article_image = ContextCompat.getDrawable(this, R.drawable.article_sample_15);

        news.c_news.add(c_news);
        news_count++;

        //최종적으로 만들어진 데이터를 어댑터에 장착//
        mAdapter.setNews(news);
        mAdapter.setNews_count(news_count);
    }

    public void re_initData() {
        //해당 데이터 초기화 부분은 네트워크(JSON, xml)로 파싱 후 가져온다.//
        //헤더와 각 뉴스에 맞게 데이터를 초기화.//

        /*Test data*/

        news.news_list = "조선일보 / 동아일보 / 매일경제 / 한국일보";
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

    public void scrapted_initData() {
        /** 서버나 데이터베이스가 구축되어 있는 경우 다시 데이터를 가져온다. **/
        //해당 데이터 초기화 부분은 네트워크(JSON, xml)로 파싱 후 가져온다.//
        //헤더와 각 뉴스에 맞게 데이터를 초기화.//

        /*Test data*/

        news.news_list = "조선일보 / 동아일보 / 매일경제 / 한국일보";
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
        smAdapter.setNews(news);
    }

    public void enroll_my_news_data() {
        //현재 저장된 나의 스크랩 배열 정보를 확인.//
        /** 서버로 해당 데이터 전송 **/

        Log.d("message : ", "my scrapt news data enroll...");

        for (int i = 0; i < news.a_news.size(); i++) {
            Log.d("a news data : ", "[" + i + "]" + news.a_news.get(i).headline_str);
        }

        for (int i = 0; i < news.b_news.size(); i++) {
            Log.d("b news data : ", "[" + i + "]" + news.b_news.get(i).headline_str);
        }

        for (int i = 0; i < news.c_news.size(); i++) {
            Log.d("c news data : ", "[" + i + "]" + news.c_news.get(i).headline_str);
        }
    }
}
