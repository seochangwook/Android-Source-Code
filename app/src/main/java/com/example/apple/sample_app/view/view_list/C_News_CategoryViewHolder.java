package com.example.apple.sample_app.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;


/**
 * Created by apple on 2016. 7. 24..
 */
public class C_News_CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView c_news_title;
    ImageView c_news_icon;

    public C_News_CategoryViewHolder(View itemView) {
        super(itemView);

        c_news_icon = (ImageView) itemView.findViewById(R.id.c_news_icon);
        c_news_title = (TextView) itemView.findViewById(R.id.c_news_title);
    }

    public void set_C_news_category(String news_title, int news_icon) {
        c_news_title.setText(news_title);
        c_news_icon.setImageResource(news_icon);
    }
}
