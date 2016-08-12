package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Chat_Friend;

/**
 * Created by apple on 2016. 8. 11..
 */
public class Chat_FriendListViewHolder extends RecyclerView.ViewHolder {
    public TextView user_name_text;
    public TextView user_email_text;
    Chat_Friend chat_friend;

    public Chat_FriendListViewHolder(View itemView) {
        super(itemView);

        user_name_text = (TextView) itemView.findViewById(R.id.user_name_text);
        user_email_text = (TextView) itemView.findViewById(R.id.user_email_text);
    }

    public void set_Chat_Friend(Chat_Friend chat_friend) {
        this.chat_friend = chat_friend;

        if (chat_friend.get_username() != null) {
            user_name_text.setText(chat_friend.get_username());
        } else {
            user_name_text.setText("이름이 등록되지 않았습니다.");
        }

        if (chat_friend.get_useremail() != null) {
            user_email_text.setText(chat_friend.get_useremail());
        } else {
            user_email_text.setText("메일이 등록되지 않았습니다.");
        }
    }
}
