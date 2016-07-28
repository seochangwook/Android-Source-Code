package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;


/**
 * Created by apple on 2016. 7. 25..
 */
public class D_News_CategoryViewHolder extends RecyclerView.ViewHolder {
    ImageView d_news_icon;
    TextView d_news_title;

    public D_News_CategoryViewHolder(View itemView) {
        super(itemView);

        d_news_icon = (ImageView) itemView.findViewById(R.id.d_news_icon);
        d_news_title = (TextView) itemView.findViewById(R.id.d_news_title);
    }

    public void set_D_news_category(String news_title, int news_icon) {
        d_news_title.setText(news_title);
        d_news_icon.setImageResource(news_icon);
    }
}
