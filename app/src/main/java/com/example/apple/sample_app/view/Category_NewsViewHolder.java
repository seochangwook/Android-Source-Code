package com.example.apple.sample_app.view;

import android.content.Context;
import android.graphics.PointF;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Category_News;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;
import jp.wasabeef.picasso.transformations.gpu.SwirlFilterTransformation;

/**
 * Created by apple on 2016. 8. 7..
 */
public class Category_NewsViewHolder extends RecyclerView.ViewHolder {
    Category_News category_news;

    ImageView category_imageview;
    TextView category_name_text;
    TextView category_code_text;

    public Category_NewsViewHolder(View itemView) {
        super(itemView);

        category_imageview = (ImageView) itemView.findViewById(R.id.category_imageview);
        category_name_text = (TextView) itemView.findViewById(R.id.category_name_text);
        category_code_text = (TextView) itemView.findViewById(R.id.category_code_text);
    }

    public void set_Category_news(Category_News category_news, Context context) {
        this.category_news = category_news;

        category_name_text.setText(category_news.get_category_name());
        category_code_text.setText("" + category_news.get_category_code());

        String category_image_url = category_news.get_category_imageUrl();

        //피카소를 이용해서 이미지 로딩.//
        //Picaso는 기본적으로 glide과 유사하고 BitmapHunter가 백그라운드로 이미지 작업을 하고 캐시기능과 이미지 후처리등 다양한 방법이 존재.//
        Picasso.with(context)
                .load(category_image_url)
                .transform(new CropCircleTransformation()) //Picaso라이브러리의 Transform적용.//
                .transform(new SwirlFilterTransformation(context, 0.5f, 1.0f, new PointF(0.5f, 0.5f)))
                .into(category_imageview); //into로 보낼 위젯 선택.//
    }
}
