package com.example.apple.sample_app.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Game_News;
import com.example.apple.sample_app.view.Game_NewsViewHolder;

/**
 * Created by apple on 2016. 8. 8..
 */
public class Game_News_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Game_News game_news;
    Context context;

    public Game_News_Adapter(Context context) {
        this.context = context;

        game_news = new Game_News();
    }

    public void set_game_News(Game_News game_news) {
        if (this.game_news != game_news) {
            this.game_news = game_news;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_gamenews, parent, false);

        Game_NewsViewHolder holder = new Game_NewsViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < game_news.game_news_array.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final Game_NewsViewHolder game_newsViewHolder = (Game_NewsViewHolder) holder;

            game_newsViewHolder.set_Game_News(game_news.game_news_array.get(position), context); //해당 포지션에 위치한 배열의 값으로 셋팅.//
        }
    }

    @Override
    public int getItemCount() {
        return game_news.game_news_array.size();
    }
}
