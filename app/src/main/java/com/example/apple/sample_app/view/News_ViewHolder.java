package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.News;


/**
 * Created by apple on 2016. 7. 24..
 */
public class News_ViewHolder extends RecyclerView.ViewHolder {
    //사용할 객체 생성.//
    News news;

    //NewsViewHolder에서 사용할 위젯정의//
    TextView news_category_array_text;
    TextView total_news_count_text;
    TextView get_news_data_text;
    ImageView icon_image;

    public News_ViewHolder(View itemView) {
        super(itemView);

        //위젯 초기화//
        news_category_array_text = (TextView) itemView.findViewById(R.id.news_category_text);
        total_news_count_text = (TextView) itemView.findViewById(R.id.news_count_text);
        get_news_data_text = (TextView) itemView.findViewById(R.id.get_news_date_text);
        icon_image = (ImageView) itemView.findViewById(R.id.icon_image);
    }

    public void setNews(News news) {
        this.news = news;

        news_category_array_text.setText(news.news_list);
        total_news_count_text.setText("" + news.total_news_count);
        get_news_data_text.setText(news.get_news_date);
        icon_image.setImageResource(news.info_icon);
    }
}
