package com.example.apple.sample_app.widget.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Trans_Data.ImageData;
import com.example.apple.sample_app.view.view_list.ImageData_ViewHolder;

/**
 * Created by apple on 2016. 8. 12..
 */
public class ImageSetAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ImageData image_data;
    Context context;

    public ImageSetAdapter(Context context) {
        this.context = context;

        image_data = new ImageData();
    }

    public void set_image_data(ImageData image_data) {
        if (this.image_data != image_data) {
            this.image_data = image_data;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_imagedata, parent, false);

        ImageData_ViewHolder holder = new ImageData_ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < image_data.imagelist.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final ImageData_ViewHolder imageData_ViewHolder = (ImageData_ViewHolder) holder;

            imageData_ViewHolder.set_ImageData(image_data.imagelist.get(position), context); //해당 포지션에 위치한 배열의 값으로 셋팅.//
        }
    }

    @Override
    public int getItemCount() {
        return image_data.imagelist.size();
    }
}
