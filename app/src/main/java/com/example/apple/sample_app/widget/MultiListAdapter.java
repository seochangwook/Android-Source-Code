package com.example.apple.sample_app.widget;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.sample_app.News_ViewActivity;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.News;
import com.example.apple.sample_app.view.A_NewsViewHolder;
import com.example.apple.sample_app.view.A_News_CategoryViewHolder;
import com.example.apple.sample_app.view.B_NewsViewHolder;
import com.example.apple.sample_app.view.B_News_CategoryViewHolder;
import com.example.apple.sample_app.view.C_NewsViewHolder;
import com.example.apple.sample_app.view.C_News_CategoryViewHolder;
import com.example.apple.sample_app.view.D_NewsViewHolder;
import com.example.apple.sample_app.view.D_News_CategoryViewHolder;
import com.example.apple.sample_app.view.News_ViewHolder;

/**
 * Created by apple on 2016. 7. 24..
 */
public class MultiListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_A_NEWS_CATEGORY = 10;
    private static final int VIEW_TYPE_B_NEWS_CATEGORY = 20;
    private static final int VIEW_TYPE_C_NEWS_CATEGORY = 30;
    private static final int VIEW_TYPE_D_NEWS_CATEGORY = 40;
    private static final int VIEW_TYPE_NEWS_INFO = 0;
    private static final int VIEW_TYPE_A_NEWS_INFO = 1;
    private static final int VIEW_TYPE_B_NEWS_INFO = 2;
    private static final int VIEW_TYPE_C_NEWS_INFO = 3;
    private static final int VIEW_TYPE_D_NEWS_INFO = 4;
    private static final String KEY_NEWS_TITLE = "news title";
    private static final String KEY_ARTICLE_HEADLINE = "article headline";
    private static final String KEY_ARTICLE_GOOD_COUNT = "good count";
    private static final String KEY_ARTICLE_BAD_COUNT = "bad count";
    //이미지는 네트워크로 받아오나 현재 요기서는 Dummy Data로 처리//
    private static final String KEY_ARTICLE_IMAGE = "article image";
    private static final String KEY_NEWS_CATEGORY_IMAGE = "news category image";
    /**
     * Data
     **/
    public String news_title; //뉴스 종류이름.//
    Context context; //액티비티의 자원을 받을 context정의.//
    News news; //클래스 정의.//

    //액티비티의 자원을 받는 생성자.//
    public MultiListAdapter(Context context) {
        this.context = context;
    }

    //데이터의 변경유무를 확인하는 메소드. 데이터의 관리는 어댑터에서 한다.//
    public void setNews(News news) {
        if (this.news != news) {
            this.news = news;

            notifyDataSetChanged();
        }
    }

    public void setNews_count(int new_count) {
        int original_count = this.news.get_total_news_count();

        int update_count = original_count + new_count;

        this.news.set_total_news_count(update_count);

        notifyDataSetChanged();
    }

    /*멀티어댑터를 사용하기 위한 콜백메소드
    멀티이니 각 뷰에 속성을 정의.*/
    @Override
    public int getItemViewType(int position) {
        if (position == 0) //리스트이 가장 첫번째 셀이 경우.//
        {
            return VIEW_TYPE_NEWS_INFO;
        }

        position--;

        //A_News에 대한 경우//
        if (news.a_news.size() > 0) {
            if (position == 0) //A_news에 대한 카테고리 경우.//
            {
                return VIEW_TYPE_A_NEWS_CATEGORY;
            }

            position--; //다시 0부터 시작하기 위해서 position조정//

            if (position < news.a_news.size()) //A_News에 대한 정보 출력//
            {
                return VIEW_TYPE_A_NEWS_INFO;
            }

            position -= news.a_news.size();
        }

        //B_News에 대한 경우//
        if (news.b_news.size() > 0) {
            if (position == 0) //B_news에 대한 카테고리 경우//
            {
                return VIEW_TYPE_B_NEWS_CATEGORY;
            }

            position--;

            if (position < news.b_news.size()) {
                return VIEW_TYPE_B_NEWS_INFO;
            }

            position -= news.b_news.size();
        }

        //C_News에 대한 경우//
        if (news.c_news.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_C_NEWS_CATEGORY;
            }

            position--;

            if (position < news.c_news.size()) {
                return VIEW_TYPE_C_NEWS_INFO;
            }

            position -= news.c_news.size();
        }

        //D뉴스의 경우//
        if (news.d_news.size() > 0) {
            if (position == 0) {
                return VIEW_TYPE_D_NEWS_CATEGORY;
            }

            position--;

            if (position < news.d_news.size()) {
                return VIEW_TYPE_D_NEWS_INFO;
            }

            position -= news.d_news.size();
        }

        //배열 인덱스 예외가 발생할 수 있으니 예외처리.//
        throw new IllegalArgumentException(("Invalid position"));
    }

    /* 기존 ListView의 getView()는 부 초기화와 데이터 셋팅을 같이 했는데 RecyclerView는 이를 분리했다. 더 효율적*/

    /**
     * 각 뷰들을 생성
     **/
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NEWS_INFO: {
                //xml로 작성된 레이아웃을 뷰로 만들기(팽창 : inflate))//
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_news, parent, false);

                News_ViewHolder holder = new News_ViewHolder(view); //뷰를 생성//

                return holder; //만들어진 뷰를 반환//
            }

            case VIEW_TYPE_A_NEWS_CATEGORY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_a_news_category, parent, false);

                A_News_CategoryViewHolder holder = new A_News_CategoryViewHolder(view);

                return holder;
            }

            case VIEW_TYPE_A_NEWS_INFO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_a_news, parent, false);

                A_NewsViewHolder holder = new A_NewsViewHolder(view);

                holder.a_news_scraptbutton.setVisibility(View.GONE); //뷰를 안보이게 하기. 현재 어댑터에서는 보이지 않게 한다.//

                return holder;
            }

            case VIEW_TYPE_B_NEWS_CATEGORY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_b_news_category, parent, false);

                B_News_CategoryViewHolder holder = new B_News_CategoryViewHolder(view);

                return holder;
            }

            case VIEW_TYPE_B_NEWS_INFO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_b_news, parent, false);

                B_NewsViewHolder holder = new B_NewsViewHolder(view);

                holder.b_news_scraptbutton.setVisibility(View.GONE);

                return holder;
            }

            case VIEW_TYPE_C_NEWS_CATEGORY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_c_news_category, parent, false);

                C_News_CategoryViewHolder holder = new C_News_CategoryViewHolder(view);

                return holder;
            }

            case VIEW_TYPE_C_NEWS_INFO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_c_news, parent, false);

                C_NewsViewHolder holder = new C_NewsViewHolder(view);

                holder.c_news_scraptbutton.setVisibility(View.GONE);

                return holder;
            }

            case VIEW_TYPE_D_NEWS_CATEGORY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_d_news_category, parent, false);

                D_News_CategoryViewHolder holder = new D_News_CategoryViewHolder(view);

                return holder;
            }

            case VIEW_TYPE_D_NEWS_INFO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_d_news, parent, false);

                D_NewsViewHolder holder = new D_NewsViewHolder(view);

                holder.d_news_scraptbutton.setVisibility(View.GONE);

                return holder;
            }
        }

        throw new IllegalArgumentException("invalid viewtype");
    }

    /**
     * 생성된 뷰에 데이터 연결 및 이벤트 처리
     **/
    //onBindView내부에서 이벤트 처리를 다 해준다.//
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (position == 0) //뷰의 첫번째 셀에 오게되는 경우.//
        {
            //알맞은 뷰의 타입으로 캐스팅//
            News_ViewHolder newsview = (News_ViewHolder) holder;

            //데이터 셋팅//
            newsview.setNews(news);

            return;
        }

        position--; //다시 0부터 시작//

        //A_News의 경우//
        if (news.a_news.size() > 0)
        {
            if (position == 0) //A_News에 대한 카테고리 설정.//
            {
                A_News_CategoryViewHolder a_news_categoryViewHolder = (A_News_CategoryViewHolder) holder;

                a_news_categoryViewHolder.set_A_news_category("조선일보", R.drawable.apple_image_2);

                news_title = a_news_categoryViewHolder.a_news_title.getText().toString(); //카테고리를 획득//

                return;
            }

            position--; //다시 0부터 시작//

            //A_news의 개수만큼 채워준다.//
            if (position < news.a_news.size()) {
                /** 객체생성 및 데이터 초기화 **/
                final A_NewsViewHolder a_news = (A_NewsViewHolder) holder;
                final String article_number = ""+position;

                a_news.set_A_news(news.a_news.get(position)); //해당 포지션에 위치한 배열의 값으로 셋팅.//

                /** A_news 버튼 관련 이벤트 처리 **/
                //A_news의 뷰에 있는 버튼을 참조하여 이벤트 처리를 한다.//
                a_news.like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_button_count = Integer.parseInt(a_news.like_button_text.getText().toString());

                        Log.d("button click", "A_news like button click : " + like_button_count);

                        Toast.makeText(context, "" + ((A_NewsViewHolder) holder).headline_text.getText() + "좋아요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                a_news.false_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int false_button_count = Integer.parseInt(a_news.false_button_text.getText().toString());

                        Log.d("button click", "A_news false button count : " + false_button_count);

                        Toast.makeText(context, "" + ((A_NewsViewHolder) holder).headline_text.getText() + "싫어요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                ((A_NewsViewHolder) holder).article_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //선택된 position에 맞는 데이터값 가져오기//
                        String get_headline = ((A_NewsViewHolder) holder).headline_text.getText().toString();
                        String like_count = ((A_NewsViewHolder) holder).like_button_text.getText().toString();
                        String false_count = ((A_NewsViewHolder) holder).false_button_text.getText().toString();

                        Log.d("headline : ", get_headline + "/" + "like count : " + like_count + "/" + "false count : " + false_count);

                        Toast.makeText(context, "기사제목 : " + get_headline + "/like count : " + like_count + "/false count : " + false_count
                                        + "뉴스종류 : " + news_title + "기사번호 : " + article_number,
                                Toast.LENGTH_SHORT).show();

                        //필요한 데이터를 지정 후 해당 상세기사보기 화면으로 이동.(실제 서버랑 연동되어 있을 시는 데이터베이스에 질의를 할
                        //특정 값만 보낸다.//
                        Intent intent = new Intent(context, News_ViewActivity.class);

                        intent.putExtra(KEY_NEWS_TITLE, news_title);
                        intent.putExtra(KEY_ARTICLE_HEADLINE, get_headline);
                        intent.putExtra(KEY_ARTICLE_GOOD_COUNT, like_count);
                        intent.putExtra(KEY_ARTICLE_BAD_COUNT, false_count);
                        intent.putExtra(KEY_NEWS_CATEGORY_IMAGE, "1");
                        intent.putExtra(KEY_ARTICLE_IMAGE, article_number + "/1");

                        context.startActivity(intent);
                    }
                });

                return;
            }

            position -= news.a_news.size();
        }

        //B_News의 경우//
        if (news.b_news.size() > 0) {
            if (position == 0) //카테고리 영역//
            {
                B_News_CategoryViewHolder b_news_categoryViewHolder = (B_News_CategoryViewHolder) holder;

                b_news_categoryViewHolder.set_B_news_category("동아일보", R.drawable.apple_image_3);

                news_title = b_news_categoryViewHolder.b_news_title.getText().toString();

                return;
            }

            position--;

            //B_News의 개수만큼 채워준다.//
            if (position < news.b_news.size()) {
                final B_NewsViewHolder b_news = (B_NewsViewHolder) holder;
                final String article_number = ""+position;

                b_news.set_B_news(news.b_news.get(position)); //해당 포지션에 위치한 배열의 값으로 셋팅.//

                /** B news관련 이벤트 처리 **/
                b_news.like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_button_count = Integer.parseInt(b_news.b_news_like_button_count_text.getText().toString());

                        Log.d("button click", "B_news like button click : " + like_button_count);

                        Toast.makeText(context, "" + ((B_NewsViewHolder) holder).headline_text.getText() + "좋아요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                b_news.false_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int false_button_count = Integer.parseInt(b_news.b_news_false_button_count_text.getText().toString());

                        Log.d("button click", "B_news false button click : " + false_button_count);

                        Toast.makeText(context, "" + ((B_NewsViewHolder) holder).headline_text.getText() + "싫어요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                ((B_NewsViewHolder) holder).article_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //선택된 position에 맞는 데이터값 가져오기//
                        String get_headline = ((B_NewsViewHolder) holder).headline_text.getText().toString();
                        String like_count = ((B_NewsViewHolder) holder).b_news_like_button_count_text.getText().toString();
                        String false_count = ((B_NewsViewHolder) holder).b_news_false_button_count_text.getText().toString();

                        Log.d("headline : ", get_headline + "/" + "like count : " + like_count + "/" + "false count : " + false_count);

                        Toast.makeText(context, "기사제목 : " + get_headline + "/like count : " + like_count + "/false count : " + false_count
                                        + "뉴스종류 : " + news_title + "기사번호 : " + article_number,
                                Toast.LENGTH_SHORT).show();

                        //필요한 데이터를 지정 후 해당 상세기사보기 화면으로 이동.(실제 서버랑 연동되어 있을 시는 데이터베이스에 질의를 할
                        //특정 값만 보낸다.//
                        Intent intent = new Intent(context, News_ViewActivity.class);

                        intent.putExtra(KEY_NEWS_TITLE, news_title);
                        intent.putExtra(KEY_ARTICLE_HEADLINE, get_headline);
                        intent.putExtra(KEY_ARTICLE_GOOD_COUNT, like_count);
                        intent.putExtra(KEY_ARTICLE_BAD_COUNT, false_count);
                        intent.putExtra(KEY_NEWS_CATEGORY_IMAGE, "2");
                        intent.putExtra(KEY_ARTICLE_IMAGE, article_number + "/2");

                        context.startActivity(intent);
                    }
                });

                return;
            }

            position -= news.a_news.size();
        }

        //C_News의 경우//
        if (news.c_news.size() > 0) {
            if (position == 0) {
                C_News_CategoryViewHolder c_news_categoryViewHolder = (C_News_CategoryViewHolder) holder;

                c_news_categoryViewHolder.set_C_news_category("매일경제", R.drawable.apple_image_4);

                news_title = c_news_categoryViewHolder.c_news_title.getText().toString();

                return;
            }

            position--;

            if (position < news.c_news.size()) {
                final C_NewsViewHolder c_news_viewHolder = (C_NewsViewHolder) holder;
                final String article_number = ""+position;

                c_news_viewHolder.set_C_news(news.c_news.get(position));

                /** C news관련 이벤트 처리 **/
                c_news_viewHolder.like_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int like_button_count = Integer.parseInt(c_news_viewHolder.c_like_button_count.getText().toString());

                        Log.d("button click", "B_news like button click : " + like_button_count);

                        Toast.makeText(context, "" + ((C_NewsViewHolder) holder).headline_text.getText() + "좋아요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                c_news_viewHolder.false_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int false_button_count = Integer.parseInt(c_news_viewHolder.c_false_button_count.getText().toString());

                        Log.d("button click", "B_news false button click : " + false_button_count);

                        Toast.makeText(context, "" + ((C_NewsViewHolder) holder).headline_text.getText() + "싫어요 증가", Toast.LENGTH_SHORT).show();
                    }
                });

                ((C_NewsViewHolder) holder).article_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //선택된 position에 맞는 데이터값 가져오기//
                        String get_headline = ((C_NewsViewHolder) holder).headline_text.getText().toString();
                        String like_count = ((C_NewsViewHolder) holder).c_like_button_count.getText().toString();
                        String false_count = ((C_NewsViewHolder) holder).c_false_button_count.getText().toString();

                        Log.d("headline : ", get_headline + "/" + "like count : " + like_count + "/" + "false count : " + false_count);

                        Toast.makeText(context, "기사제목 : " + get_headline + "/like count : " + like_count + "/false count : " + false_count
                                        + "뉴스종류 : " + news_title + "기사번호 : " + article_number,
                                Toast.LENGTH_SHORT).show();

                        //필요한 데이터를 지정 후 해당 상세기사보기 화면으로 이동.(실제 서버랑 연동되어 있을 시는 데이터베이스에 질의를 할
                        //특정 값만 보낸다.//
                        Intent intent = new Intent(context, News_ViewActivity.class);

                        intent.putExtra(KEY_NEWS_TITLE, news_title);
                        intent.putExtra(KEY_ARTICLE_HEADLINE, get_headline);
                        intent.putExtra(KEY_ARTICLE_GOOD_COUNT, like_count);
                        intent.putExtra(KEY_ARTICLE_BAD_COUNT, false_count);
                        intent.putExtra(KEY_NEWS_CATEGORY_IMAGE, "3");
                        intent.putExtra(KEY_ARTICLE_IMAGE, article_number + "/3");

                        context.startActivity(intent);
                    }
                });

                return;
            }

            position -= news.c_news.size();
        }

        //D뉴스의 경우.//
        if (news.d_news.size() > 0) {
            if (position == 0) {
                D_News_CategoryViewHolder d_news_categoryViewHolder = (D_News_CategoryViewHolder) holder;

                d_news_categoryViewHolder.set_D_news_category("한국일보", R.drawable.apple_image_6);

                return;
            }

            position--;

            if (position < news.d_news.size()) {
                D_NewsViewHolder d_news_viewHolder = (D_NewsViewHolder) holder;

                d_news_viewHolder.set_D_news(news.d_news.get(position));

                /** D news관련 이벤트 처리 **/
                ((D_NewsViewHolder) holder).article_imageview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //선택된 position에 맞는 데이터값 가져오기//
                        String get_headline = ((D_NewsViewHolder) holder).headline_text.getText().toString();
                        String like_count = ((D_NewsViewHolder) holder).like_button_text.getText().toString();
                        String false_count = ((D_NewsViewHolder) holder).false_button_text.getText().toString();

                        Log.d("headline : ", get_headline + "/" + "like count : " + like_count + "/" + "false count : " + false_count);

                        Toast.makeText(context, "기사제목 : " + get_headline + "/like count : " + like_count + "/false count : " + false_count,
                                Toast.LENGTH_SHORT).show();
                    }
                });

                return;
            }

            position -= news.d_news.size();
        }

        throw new IllegalArgumentException("invalid position");
    }

    //전체적인 뷰의 개수를 설정.//
    @Override
    public int getItemCount() {
        if (news == null) {
            return 0;
        }

        int count = 0;

        count++; //첫번째 셀에 관한 경우의 카운터//

        //A_News의 개수판단.//
        if (news.a_news.size() > 0) {
            count += (1 + news.a_news.size());
        }

        //B_News의 개수판단.//
        if (news.b_news.size() > 0) {
            count += (1 + news.b_news.size());
        }

        if (news.c_news.size() > 0) {
            count += (1 + news.c_news.size());
        }

        if (news.d_news.size() > 0) {
            count += (1 + news.d_news.size());
        }

        return count;
    }
}
