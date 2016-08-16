package com.example.apple.sample_app.widget.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.View_Data.Full_News;
import com.example.apple.sample_app.view.view_list.Full_A_NewsViewHolder;
import com.example.apple.sample_app.view.view_list.Full_A_News_CategoryViewHolder;
import com.example.apple.sample_app.view.view_list.Full_NewsViewHolder;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_News_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_FULL_A_NEWS_INFO = 10;
    private static final int VIEW_TYPE_FULL_A_NEWS_CATEGORY = 20;
    private static final int VIEW_TYPE_FULL_NEWS_INFO = 0;
    Full_News full_news;
    Context context;

    public Full_News_Adapter(Context context) {
        this.context = context;
    }

    public void set_Full_News(Full_News full_news) {
        if (this.full_news != full_news) {
            this.full_news = full_news;

            notifyDataSetChanged();
        }
    }

    public void set_Full_News_count(int total_count) {
        int original_count = this.full_news.get_total_news_count();

        int update_count = original_count + total_count;

        full_news.set_total_news_count(update_count);

        notifyDataSetChanged();
    }

    /*멀티어댑터를 사용하기 위한 콜백메소드
    멀티이니 각 뷰에 속성을 정의.*/
    @Override
    public int getItemViewType(int position) {
        if (position == 0) //리스트이 가장 첫번째 셀이 경우.//
        {
            return VIEW_TYPE_FULL_NEWS_INFO;
        }

        position--;

        //Full_A_News에 대한 경우//
        if (full_news.full_a_newsList.size() > 0) {
            if (position == 0) //A_news에 대한 카테고리 경우.//
            {
                return VIEW_TYPE_FULL_A_NEWS_CATEGORY;
            }

            position--; //다시 0부터 시작하기 위해서 position조정//

            if (position < full_news.full_a_newsList.size()) //A_News에 대한 정보 출력//
            {
                return VIEW_TYPE_FULL_A_NEWS_INFO;
            }

            position -= full_news.full_a_newsList.size();
        }

        //배열 인덱스 예외가 발생할 수 있으니 예외처리.//
        throw new IllegalArgumentException(("Invalid position"));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_FULL_NEWS_INFO: {
                //xml로 작성된 레이아웃을 뷰로 만들기(팽창 : inflate))//
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_fullnews, parent, false);

                Full_NewsViewHolder holder = new Full_NewsViewHolder(view); //뷰를 생성//

                return holder; //만들어진 뷰를 반환//
            }

            case VIEW_TYPE_FULL_A_NEWS_CATEGORY: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_full_a_news_category, parent, false);

                Full_A_News_CategoryViewHolder holder = new Full_A_News_CategoryViewHolder(view); //뷰를 생성//

                return holder; //만들어진 뷰를 반환//
            }

            case VIEW_TYPE_FULL_A_NEWS_INFO: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_full_a_news, parent, false);

                Full_A_NewsViewHolder holder = new Full_A_NewsViewHolder(view);

                return holder;
            }
        }

        throw new IllegalArgumentException("invalid viewtype");
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //데이터를 초기화.//
        if (position == 0) //뷰의 첫번째 셀에 오게되는 경우.//
        {
            //알맞은 뷰의 타입으로 캐스팅//
            Full_NewsViewHolder full_newsview = (Full_NewsViewHolder) holder;

            //데이터 셋팅//
            full_newsview.set_Full_News(full_news);

            return;
        }

        position--; //다시 0부터 시작//

        //A_News의 경우//
        if (full_news.full_a_newsList.size() > 0) {
            if (position == 0) //A_News에 대한 카테고리 설정.//
            {
                Full_A_News_CategoryViewHolder full_a_news_categoryViewHolderr = (Full_A_News_CategoryViewHolder) holder;

                full_a_news_categoryViewHolderr.set_A_news_category("RPG게임", R.drawable.apple_image_2);

                return;
            }

            position--; //다시 0부터 시작//

            //A_news의 개수만큼 채워준다.//
            if (position < full_news.full_a_newsList.size()) {
                /** 객체생성 및 데이터 초기화 **/
                final Full_A_NewsViewHolder full_a_news_viewholder = (Full_A_NewsViewHolder) holder;

                full_a_news_viewholder.set_Full_A_News(full_news.full_a_newsList.get(position), context); //해당 포지션에 위치한 배열의 값으로 셋팅.//

                return;
            }

            position -= full_news.full_a_newsList.size();
        }

        position -= full_news.full_a_newsList.size();

        throw new IllegalArgumentException("invalid position");
    }

    @Override
    public int getItemCount() {
        if (full_news == null) {
            return 0;
        }

        int count = 0;

        count++; //첫번째 셀에 관한 경우의 카운터//

        //A_News의 개수판단.//
        if (full_news.full_a_newsList.size() > 0) {
            count += (1 + full_news.full_a_newsList.size());
        }

        return count;
    }
}
