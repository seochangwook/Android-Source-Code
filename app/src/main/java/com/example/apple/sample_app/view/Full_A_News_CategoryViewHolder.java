package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_A_News_CategoryViewHolder extends RecyclerView.ViewHolder {
    TextView full_a_news_name_text;
    ImageView full_a_news_name_imageview;

    public Full_A_News_CategoryViewHolder(View itemView) {
        super(itemView);

        full_a_news_name_text = (TextView) itemView.findViewById(R.id.full_a_news_title);
        full_a_news_name_imageview = (ImageView) itemView.findViewById(R.id.full_a_news_icon);
    }

    public void set_A_news_category(String news_title, int news_icon) {
        full_a_news_name_text.setText(news_title);
        full_a_news_name_imageview.setImageResource(news_icon);
    }
}
