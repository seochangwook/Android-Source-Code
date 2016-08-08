package com.example.apple.sample_app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Game_News;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

/**
 * Created by apple on 2016. 8. 8..
 */
public class Game_NewsViewHolder extends RecyclerView.ViewHolder {
    public ImageView thumbnail_imageview;
    public TextView game_name_text;
    public TextView game_description_text;
    public TextView game_tinyUrl_text;
    public TextView game_webUrl_text;
    Game_News game_news;

    public Game_NewsViewHolder(View itemView) {
        super(itemView);

        thumbnail_imageview = (ImageView) itemView.findViewById(R.id.game_news_thumbnail);
        game_name_text = (TextView) itemView.findViewById(R.id.game_name_text);
        game_description_text = (TextView) itemView.findViewById(R.id.game_description_text);
        game_tinyUrl_text = (TextView) itemView.findViewById(R.id.game_tinyurl_text);
        game_webUrl_text = (TextView) itemView.findViewById(R.id.game_weburl_text);
    }

    public void set_Game_News(Game_News game_news, Context context) {
        this.game_news = game_news;

        String thumbnailUrl_str = game_news.get_thumbnailUrl();

        //피카소를 이용해서 이미지 로딩.//
        //Picaso는 기본적으로 glide과 유사하고 BitmapHunter가 백그라운드로 이미지 작업을 하고 캐시기능과 이미지 후처리등 다양한 방법이 존재.//
        Picasso.with(context)
                .load(thumbnailUrl_str)
                .transform(new CropCircleTransformation())
                .into(thumbnail_imageview); //into로 보낼 위젯 선택.//

        game_name_text.setText(game_news.get_name());
        game_description_text.setText(game_news.get_description());
        game_webUrl_text.setText(game_news.get_webUrl());
        game_tinyUrl_text.setText(game_news.get_tinyUrl());
    }
}
