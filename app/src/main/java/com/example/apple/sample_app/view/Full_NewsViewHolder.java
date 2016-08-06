package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Full_News;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_NewsViewHolder extends RecyclerView.ViewHolder {
    Full_News full_news;

    TextView total_full_news_count_text;

    public Full_NewsViewHolder(View itemView) {
        super(itemView);

        total_full_news_count_text = (TextView) itemView.findViewById(R.id.full_news_count_text);
    }

    public void set_Full_News(Full_News full_news) {
        this.full_news = full_news;

        total_full_news_count_text.setText("" + full_news.get_total_news_count());
    }
}
