package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.A_News;


/**
 * Created by apple on 2016. 7. 24..
 */
public class A_NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView headline_text;
    public ImageView article_imageview;
    public TextView like_button_text;
    public TextView false_button_text;
    public Button like_button;
    public Button false_button;
    public CheckBox a_news_scraptbutton;
    A_News a_news;

    public A_NewsViewHolder(View itemView) {
        super(itemView);

        headline_text = (TextView) itemView.findViewById(R.id.a_news_headline_text);
        article_imageview = (ImageView) itemView.findViewById(R.id.a_news_article_imageview);
        like_button = (Button) itemView.findViewById(R.id.a_news_like_button);
        false_button = (Button) itemView.findViewById(R.id.a_news_false_button);
        like_button_text = (TextView) itemView.findViewById(R.id.a_news_like_count_text);
        false_button_text = (TextView) itemView.findViewById(R.id.a_news_false_count_text);
        a_news_scraptbutton = (CheckBox) itemView.findViewById(R.id.a_news_check_news);
    }

    public void set_A_news(A_News a_news) {
        this.a_news = a_news;

        headline_text.setText(a_news.headline_str);
        article_imageview.setImageDrawable(a_news.article_image);
    }
}
