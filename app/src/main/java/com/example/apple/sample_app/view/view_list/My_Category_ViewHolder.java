package com.example.apple.sample_app.view.view_list;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.View_Data.My_Category;

/**
 * Created by apple on 2016. 8. 2..
 */
public class My_Category_ViewHolder extends RecyclerView.ViewHolder {
    public ImageView my_category_image;
    public Button my_category_lock_option_button;
    public TextView my_category_text;
    My_Category my_category;

    public My_Category_ViewHolder(View itemView) {
        super(itemView);

        my_category_image = (ImageView) itemView.findViewById(R.id.category_image);
        my_category_lock_option_button = (Button) itemView.findViewById(R.id.category_lock_option_button);
        my_category_text = (TextView) itemView.findViewById(R.id.category_text);
    }

    public void set_My_Category(My_Category my_category) {
        this.my_category = my_category;

        my_category_image.setImageBitmap(my_category.category_image);
        my_category_text.setText(my_category.category_name);
        this.my_category.category_lock_option = my_category.category_lock_option;
    }
}
