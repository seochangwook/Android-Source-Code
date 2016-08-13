package com.example.apple.sample_app.view;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.ImageData;

/**
 * Created by apple on 2016. 8. 12..
 */
public class ImageData_ViewHolder extends RecyclerView.ViewHolder {
    ImageData imagedata;

    ImageView imageview;
    TextView content_text;

    public ImageData_ViewHolder(View itemView) {
        super(itemView);

        imageview = (ImageView) itemView.findViewById(R.id.imagedata_view);
        content_text = (TextView) itemView.findViewById(R.id.image_content_text);
    }

    public void set_ImageData(ImageData imagedata, Context context) {
        this.imagedata = imagedata;

        content_text.setText(imagedata.get_image_content());

        String image_url = imagedata.get_image_Url();

        Glide.with(context)
                .load(image_url)
                .into(imageview); //into로 보낼 위젯 선택.//
    }
}
