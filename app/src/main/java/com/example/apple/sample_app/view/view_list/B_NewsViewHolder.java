package com.example.apple.sample_app.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.View_Data.B_News;


/**
 * Created by apple on 2016. 7. 24..
 */
public class B_NewsViewHolder extends RecyclerView.ViewHolder {
    public TextView headline_text;
    public ImageView article_imageview;
    public TextView b_news_like_button_count_text;
    public TextView b_news_false_button_count_text;
    public Button like_button;
    public Button false_button;
    public CheckBox b_news_scraptbutton;
    B_News b_news;

    /**
     * 사용할 리소스를 초기화
     **/
    public B_NewsViewHolder(View itemView) {
        super(itemView);

        headline_text = (TextView) itemView.findViewById(R.id.b_news_headline_text);
        article_imageview = (ImageView) itemView.findViewById(R.id.b_news_article_imageview);
        like_button = (Button) itemView.findViewById(R.id.b_news_like_button);
        false_button = (Button) itemView.findViewById(R.id.b_news_false_button);
        b_news_like_button_count_text = (TextView) itemView.findViewById(R.id.b_news_like_count_text);
        b_news_false_button_count_text = (TextView) itemView.findViewById(R.id.b_news_false_count_text);
        b_news_scraptbutton = (CheckBox) itemView.findViewById(R.id.b_news_check_news);
    }

    public void set_B_news(B_News b_news) {
        this.b_news = b_news;

        headline_text.setText(b_news.headline_str);
        article_imageview.setImageDrawable(b_news.article_image);
    }
}
