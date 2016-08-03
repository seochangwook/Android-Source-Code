package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;


/**
 * Created by apple on 2016. 7. 24..
 */
public class A_News_CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView a_news_title;
    ImageView a_news_icon;

    public A_News_CategoryViewHolder(View itemView) {
        super(itemView);

        a_news_icon = (ImageView) itemView.findViewById(R.id.a_news_icon);
        a_news_title = (TextView) itemView.findViewById(R.id.a_news_title);
    }

    public void set_A_news_category(String news_title, int news_icon) {
        a_news_title.setText(news_title);
        a_news_icon.setImageResource(news_icon);
    }
}
