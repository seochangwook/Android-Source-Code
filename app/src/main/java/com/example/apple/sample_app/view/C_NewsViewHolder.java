package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.C_News;

/**
 * Created by apple on 2016. 7. 24..
 */
public class C_NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView headline_text;
    public ImageView article_imageview;
    public Button like_button;
    public Button false_button;
    public TextView c_like_button_count;
    public TextView c_false_button_count;
    public CheckBox c_news_scraptbutton;
    C_News c_news;

    public C_NewsViewHolder(View itemView) {
        super(itemView);

        headline_text = (TextView) itemView.findViewById(R.id.c_news_headline_text);
        article_imageview = (ImageView) itemView.findViewById(R.id.c_news_article_imageview);
        like_button = (Button) itemView.findViewById(R.id.c_news_like_button);
        false_button = (Button) itemView.findViewById(R.id.c_news_false_button);
        c_news_scraptbutton = (CheckBox) itemView.findViewById(R.id.c_news_check_news);
        c_like_button_count = (TextView) itemView.findViewById(R.id.c_news_like_count_text);
        c_false_button_count = (TextView) itemView.findViewById(R.id.c_news_false_count_text);
    }

    public void set_C_news(C_News c_news) {
        this.c_news = c_news;

        headline_text.setText(c_news.headline_str);
        article_imageview.setImageDrawable(c_news.article_image);
    }
}
