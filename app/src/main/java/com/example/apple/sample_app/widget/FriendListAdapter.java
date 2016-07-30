package com.example.apple.sample_app.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Friend;
import com.example.apple.sample_app.view.Friend_ViewHolder;

/**
 * Created by apple on 2016. 7. 29..
 */
public class FriendListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context; //액티비티의 자원을 받을 context정의.//
    Friend friend; //객체.//

    public FriendListAdapter(Context context) //액티비티의 자원을 얻기 위해서 생성자 정의.//
    {
        this.context = context;

        friend = new Friend();
    }

    public void set_Friend(Friend friend) {
        if (this.friend != friend) {
            this.friend = friend;

            notifyDataSetChanged();
        }
    }

    //복합뷰가 아니기에 getItemViewType(int position)필요없다.//

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_friend, parent, false);

        Friend_ViewHolder holder = new Friend_ViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < friend.friend_list.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final Friend_ViewHolder friend_viewHolder = (Friend_ViewHolder) holder;

            friend_viewHolder.set_Friend_info(friend.friend_list.get(position)); //해당 포지션에 위치한 배열의 값으로 셋팅.//

            friend_viewHolder.option_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (friend_viewHolder.is_check_follow == false) //팔로우 상태일 경우.//
                    {
                        friend_viewHolder.is_check_follow = true;

                        Toast.makeText(context, "" + friend_viewHolder.friend_name_text.getText() + "팔로잉", Toast.LENGTH_SHORT).show();

                        friend_viewHolder.option_button.setBackgroundColor(Color.GREEN); //색갈변경.//
                        friend_viewHolder.option_button.setText("+팔로잉");
                    } else if (friend_viewHolder.is_check_follow == true) //팔로잉 상태일 경우.//
                    {
                        friend_viewHolder.is_check_follow = false;

                        Toast.makeText(context, "" + friend_viewHolder.friend_name_text.getText() + "팔로우", Toast.LENGTH_SHORT).show();

                        friend_viewHolder.option_button.setBackgroundColor(Color.GRAY); //색갈변경.//
                        friend_viewHolder.option_button.setText("+팔로우");
                    }
                }
            });

            return;
        }
    }

    @Override
    public int getItemCount() {
        return friend.friend_list.size(); //배열의 크기만큼 반환.//
    }
}
