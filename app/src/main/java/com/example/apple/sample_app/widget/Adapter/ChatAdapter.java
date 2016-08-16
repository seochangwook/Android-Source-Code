package com.example.apple.sample_app.widget.Adapter;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.DB_Data.ChatContract;
import com.example.apple.sample_app.view.view_list.ReceiveViewHolder;
import com.example.apple.sample_app.view.view_list.SendViewHolder;

/**
 * Created by apple on 2016. 8. 12..
 */
public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int VIEW_TYPE_SEND = 1;
    private static final int VIEW_TYPE_RECEIVE = 2;
    Cursor cursor;

    public void changeCursor(Cursor c) {
        if (cursor != null) {
            cursor.close();
        }
        cursor = c;
        notifyDataSetChanged();
    }

    //멀티타입 리사이클뷰//
    @Override
    public int getItemViewType(int position) {
        cursor.moveToPosition(position);
        int type = cursor.getInt(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_TYPE));
        switch (type) {
            case ChatContract.ChatMessage.TYPE_SEND:
                return VIEW_TYPE_SEND;
            case ChatContract.ChatMessage.TYPE_RECEIVE:
                return VIEW_TYPE_RECEIVE;
        }
        throw new IllegalArgumentException("invalid type");
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SEND: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_send, parent, false);
                return new SendViewHolder(view);
            }
            case VIEW_TYPE_RECEIVE: {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_receive, parent, false);
                return new ReceiveViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        cursor.moveToPosition(position);
//        int type = cursor.getInt(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_TYPE));
        switch (holder.getItemViewType()) {
            case VIEW_TYPE_SEND: {
                SendViewHolder svh = (SendViewHolder) holder;
                String message = cursor.getString(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_MESSAGE));
                svh.setMessage(message); //해당 뷰에 맞는 데이터를 가져와서 보여준다.//
                break;
            }

            case VIEW_TYPE_RECEIVE: {
                ReceiveViewHolder rvh = (ReceiveViewHolder) holder;
                String message = cursor.getString(cursor.getColumnIndex(ChatContract.ChatMessage.COLUMN_MESSAGE));
                rvh.setMessage(message);
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        if (cursor == null)
            return 0;

        return cursor.getCount(); //커서의 사이즈, 즉 테이블에서의 전체 투플의 개수만큼.//
    }
}