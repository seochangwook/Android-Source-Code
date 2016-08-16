package com.example.apple.sample_app.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;

/**
 * Created by apple on 2016. 7. 24..
 */
public class B_News_CategoryViewHolder extends RecyclerView.ViewHolder {
    public TextView b_news_title;
    ImageView b_news_icon;

    public B_News_CategoryViewHolder(View itemView) {
        super(itemView);

        b_news_icon = (ImageView) itemView.findViewById(R.id.b_news_icon);
        b_news_title = (TextView) itemView.findViewById(R.id.b_news_title);
    }

    public void set_B_news_category(String news_title, int news_icon) {
        b_news_title.setText(news_title);
        b_news_icon.setImageResource(news_icon);
    }
}
