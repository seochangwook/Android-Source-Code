package com.example.apple.sample_app.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.My_Category;
import com.example.apple.sample_app.view.My_Category_ViewHolder;

/**
 * Created by apple on 2016. 8. 1..
 */
public class My_Category_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context context;
    My_Category my_category;

    public My_Category_Adapter(Context context) {
        this.context = context;

        my_category = new My_Category();
    }

    public void set_Category(My_Category my_category) {
        if (this.my_category != my_category) {
            this.my_category = my_category;

            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_my_category, parent, false);

        My_Category_ViewHolder holder = new My_Category_ViewHolder(view); //뷰 생성//

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        //데이터를 만들어 준다.//
        if (position < my_category.my_category_list.size()) //데이터를 객체의 개수만큼 할당.//
        {
            final My_Category_ViewHolder my_category_viewHolder = (My_Category_ViewHolder) holder;

            my_category_viewHolder.set_My_Category(my_category.my_category_list.get(position)); //해당 포지션에 위치한 배열의 값으로 셋팅.//

            if (my_category.category_lock_option == false) {
                my_category_viewHolder.my_category_lock_option_button.setBackgroundResource(android.R.drawable.ic_partial_secure);
            }

            //버튼클릭에 따른 보안설정 변경. 다시 보안장치를 풀때는 추가적인 요구사항을 적용한다. 서버에도 적용.//
            my_category_viewHolder.my_category_lock_option_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (my_category.category_lock_option == false) {
                        Toast.makeText(context, "카테고리 비공개 설정", Toast.LENGTH_SHORT).show();

                        my_category_viewHolder.my_category_lock_option_button.setBackgroundResource(android.R.drawable.ic_secure);
                        my_category.category_lock_option = true;
                    } else if (my_category.category_lock_option == true) {
                        Toast.makeText(context, "카테고리 공개 설정", Toast.LENGTH_SHORT).show();

                        my_category_viewHolder.my_category_lock_option_button.setBackgroundResource(android.R.drawable.ic_partial_secure);
                        my_category.category_lock_option = false;
                    }
                }
            });

            my_category_viewHolder.my_category_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String category_name = my_category_viewHolder.my_category_text.getText().toString();

                    Toast.makeText(context, "" + category_name + "카테고리 방 입장", Toast.LENGTH_SHORT).show();
                }
            });

            return;
        }
    }

    @Override
    public int getItemCount() {
        return this.my_category.my_category_list.size();
    }
}
