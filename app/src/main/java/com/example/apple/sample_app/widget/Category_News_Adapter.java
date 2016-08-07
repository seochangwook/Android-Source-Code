package com.example.apple.sample_app.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Category_News;
import com.example.apple.sample_app.view.Category_NewsViewHolder;

/**
 * Created by apple on 2016. 8. 7..
 */
public class Category_News_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Category_News category_news;

    Context context;

    public Category_News_Adapter(Context context) {
        this.context = context;

        category_news = new Category_News();
    }

    public void set_Category_News(Category_News category_news) {
        if (this.category_news != category_news) {
            this.category_news = category_news;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_category_news, parent, false);

        Category_NewsViewHolder holder = new Category_NewsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < category_news.category_news_array.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final Category_NewsViewHolder category_newsViewHolder = (Category_NewsViewHolder) holder;

            category_newsViewHolder.set_Category_news(category_news.category_news_array.get(position), context); //해당 포지션에 위치한 배열의 값으로 셋팅.//
        }
    }

    @Override
    public int getItemCount() {
        return category_news.category_news_array.size(); //배열의 크기만큼 반환.//
    }
}
