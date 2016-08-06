package com.example.apple.sample_app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Full_A_News;
import com.squareup.picasso.Picasso;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_A_NewsViewHolder extends RecyclerView.ViewHolder {
    Context context;

    Full_A_News full_a_news;

    TextView item_name_text;
    TextView item_description_text;
    TextView item_tinyUrl_text;
    TextView item_webUrl_text;
    ImageView item_thumbnail_imageview;

    String item_image_url_str;

    public Full_A_NewsViewHolder(View itemView) {
        super(itemView);

        item_name_text = (TextView) itemView.findViewById(R.id.item_name_text);
        item_description_text = (TextView) itemView.findViewById(R.id.item_description_text);
        item_tinyUrl_text = (TextView) itemView.findViewById(R.id.item_tinyUrl_text);
        item_webUrl_text = (TextView) itemView.findViewById(R.id.item_weburl_text);
        item_thumbnail_imageview = (ImageView) itemView.findViewById(R.id.item_thumbnail_imageview);
    }

    public void set_Full_A_News(Full_A_News full_a_news, Context context) {
        this.context = context;
        this.full_a_news = full_a_news;

        item_name_text.setText(full_a_news.get_item_name());
        item_webUrl_text.setText(full_a_news.get_item_webUrl());
        item_description_text.setText(full_a_news.get_item_description());
        item_tinyUrl_text.setText(full_a_news.get_item_tinyUrl());

        item_image_url_str = full_a_news.get_item_thumbnailUrl();

        //피카소를 이용해서 이미지 로딩.//
        //Picaso는 기본적으로 glide과 유사하고 BitmapHunter가 백그라운드로 이미지 작업을 하고 캐시기능과 이미지 후처리등 다양한 방법이 존재.//
        Picasso.with(context)
                .load(item_image_url_str)
                .into(item_thumbnail_imageview); //into로 보낼 위젯 선택.//
    }
}
