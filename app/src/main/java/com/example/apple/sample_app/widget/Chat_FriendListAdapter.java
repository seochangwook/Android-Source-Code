package com.example.apple.sample_app.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Chat_Friend;
import com.example.apple.sample_app.view.Chat_FriendListViewHolder;

/**
 * Created by apple on 2016. 8. 11..
 */
public class Chat_FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Chat_Friend chat_friend;
    Context context;

    public Chat_FriendListAdapter(Context context) {
        this.context = context;
        chat_friend = new Chat_Friend();
    }

    public void set_Chat_FriendList(Chat_Friend chat_friend) {
        if (this.chat_friend != chat_friend) {
            this.chat_friend = chat_friend;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_chat_friendlist, parent, false);

        Chat_FriendListViewHolder holder = new Chat_FriendListViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < chat_friend.chat_friend_list.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final Chat_FriendListViewHolder chat_friend_viewHolder = (Chat_FriendListViewHolder) holder;

            chat_friend_viewHolder.set_Chat_Friend(chat_friend.chat_friend_list.get(position)); //해당 포지션에 위치한 배열의 값으로 셋팅.//
        }

        return;
    }

    @Override
    public int getItemCount() {
        return chat_friend.chat_friend_list.size();
    }
}
