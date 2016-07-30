package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Friend;

/**
 * Created by apple on 2016. 7. 30..
 */
public class Friend_ViewHolder extends RecyclerView.ViewHolder {
    public TextView friend_name_text;
    public TextView friend_email_address_text;
    public TextView friend_home_phone_text;
    public TextView friend_mobile_phone_text;
    public ImageView friend_imageview;
    public Button option_button;
    public boolean is_check_follow;
    Friend friend;

    public Friend_ViewHolder(View itemView) {
        super(itemView);

        friend_name_text = (TextView) itemView.findViewById(R.id.friend_name);
        friend_imageview = (ImageView) itemView.findViewById(R.id.friend_imageview);
        friend_email_address_text = (TextView) itemView.findViewById(R.id.friend_email);
        friend_home_phone_text = (TextView) itemView.findViewById(R.id.friend_home_phone);
        friend_mobile_phone_text = (TextView) itemView.findViewById(R.id.friend_mobile_phone);
        option_button = (Button) itemView.findViewById(R.id.option_button);
    }

    public void set_Friend_info(Friend friend) {
        this.friend = friend;

        friend_imageview.setImageDrawable(friend.get_people_image());
        friend_name_text.setText(friend.get_friend_name());
        friend_email_address_text.setText(friend.get_email_address());
        friend_home_phone_text.setText(friend.get_home_phone());
        friend_mobile_phone_text.setText(friend.get_mobild_phone());
        is_check_follow = friend.is_follow_check;
    }
}
