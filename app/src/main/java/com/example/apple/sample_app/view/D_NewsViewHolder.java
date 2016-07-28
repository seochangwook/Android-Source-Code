package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.D_News;


/**
 * Created by apple on 2016. 7. 25..
 */
public class D_NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView headline_text;
    public ImageView article_imageview;
    public Button like_button;
    public Button false_button;
    public TextView like_button_text;
    public TextView false_button_text;
    public CheckBox d_news_scraptbutton;
    D_News d_news;

    public D_NewsViewHolder(View itemView) {
        super(itemView);

        headline_text = (TextView) itemView.findViewById(R.id.d_news_headline_text);
        article_imageview = (ImageView) itemView.findViewById(R.id.d_news_article_imageview);
        like_button = (Button) itemView.findViewById(R.id.d_news_like_button);
        false_button = (Button) itemView.findViewById(R.id.d_news_false_button);
        like_button_text = (TextView) itemView.findViewById(R.id.d_news_like_count_text);
        false_button_text = (TextView) itemView.findViewById(R.id.d_news_false_count_text);
        d_news_scraptbutton = (CheckBox) itemView.findViewById(R.id.d_news_check_news);
    }

    public void set_D_news(D_News d_news) {
        this.d_news = d_news;

        headline_text.setText(d_news.headline_str);
        article_imageview.setImageBitmap(d_news.article_image);//비트맵으로 설정.//
        like_button_text.setText("" + d_news.like_count);
        false_button_text.setText("" + d_news.false_count);
    }
}
