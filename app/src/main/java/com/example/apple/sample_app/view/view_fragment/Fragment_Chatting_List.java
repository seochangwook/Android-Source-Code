package com.example.apple.sample_app.view.view_fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.apple.sample_app.ChatActivity;
import com.example.apple.sample_app.DataBase.DBManager;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.DB_Data.ChatContract;
import com.example.apple.sample_app.data.Trans_Data.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Chatting_List extends Fragment {
    ListView listView;

    SimpleCursorAdapter mAdapter;

    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//

    public Fragment_Chatting_List() {
        // Required empty publdic constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] from = {ChatContract.ChatUser.COLUMN_NAME, ChatContract.ChatUser.COLUMN_EMAIL, ChatContract.ChatMessage.COLUMN_MESSAGE};
        int[] to = {R.id.text_name, R.id.text_email, R.id.text_last_message};

        //어댑터를 SQLite와 동기화 할 수 있는 SimpleCursorAdapter를 사용.//
        mAdapter = new SimpleCursorAdapter(getContext(), R.layout.view_chat_user, null, from, to, 0);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chatting_list, container, false);

        listView = (ListView) view.findViewById(R.id.chatlist_listView);
        listView.setAdapter(mAdapter);

        listView.setId(android.R.id.list);

        //다시 채팅방으로 이동.User객체를 넘겨야 하기에 직렬화 이용)//
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //커서로 현재 리스트뷰에 해당하는 위치에 값을 데이터베이스랑 동기화 해서 얻어온다.//
                Cursor cursor = (Cursor) listView.getItemAtPosition(position);

                User user = new User();

                user.setId(cursor.getLong(cursor.getColumnIndex(ChatContract.ChatUser.COLUMN_SERVER_ID)));
                user.setUserName(cursor.getString(cursor.getColumnIndex(ChatContract.ChatUser.COLUMN_NAME)));
                user.setEmail(cursor.getString(cursor.getColumnIndex(ChatContract.ChatUser.COLUMN_EMAIL)));

                Intent intent = new Intent(getActivity(), ChatActivity.class);

                intent.putExtra(ChatActivity.EXTRA_USER, user); //객체를 전달.//

                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Cursor c = DBManager.getInstance().getChatUser(); //현재 커서는 데이터베이스의 정보를 받는다.//

        showpDialog();

        mAdapter.changeCursor(c); //changCusor를 이용하여 초기화.//

        hidepDialog();
        /** changeCursor(Cursor c) -> 새로운 Cursor를 설정하면서 기존 Cursor를 close()한다.//
         *  swapCursor(Cursor c) -> Adapter내에 이미 설정된 Cursor가 있는 경우 그 Cursor를 넘겨주고 새로운 커서를 내부에 설정
         *  기존 커서는 close()하지 않는다.
         */
    }

    @Override
    public void onStop() {
        super.onStop();

        mAdapter.changeCursor(null); //null로 해주어야지 액티비티 종료 시 자원이 적절하게 해제된다.//
    }

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
