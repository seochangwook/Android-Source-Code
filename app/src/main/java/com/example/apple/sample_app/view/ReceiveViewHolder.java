package com.example.apple.sample_app.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.apple.sample_app.R;

/**
 * Created by apple on 2016. 8. 12..
 */
public class ReceiveViewHolder extends RecyclerView.ViewHolder {
    TextView messageView;

    public ReceiveViewHolder(View itemView) {
        super(itemView);

        messageView = (TextView) itemView.findViewById(R.id.text_message);
    }

    public void setMessage(String message) {
        messageView.setText(message);
    }
}
