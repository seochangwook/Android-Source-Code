package com.example.apple.sample_app;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.example.apple.sample_app.DataBase.DBManager;
import com.example.apple.sample_app.data.DB_Data.ChatContract;
import com.example.apple.sample_app.data.Trans_Data.User;
import com.example.apple.sample_app.widget.Adapter.ChatAdapter;

public class ChatActivity extends AppCompatActivity {
    public static final String EXTRA_USER = "user";
    RecyclerView recyclerview;
    ChatAdapter mAdapter;
    RadioGroup typeView;
    EditText inputView;
    Button send_button;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerview = (RecyclerView) findViewById(R.id.rv_list);
        typeView = (RadioGroup) findViewById(R.id.group_type);
        inputView = (EditText) findViewById(R.id.edit_input);
        send_button = (Button) findViewById(R.id.btn_send);

        //넘어온 객체의 정보는 강제 캐스팅을 이용하여 받는다.//
        user = (User) getIntent().getSerializableExtra(EXTRA_USER); //직렬화된 값을 가져온다.//

        Log.d("user name : ", user.getUserName());
        Log.d("user email : ", user.getEmail());
        Log.d("user id : ", "" + user.getId());

        mAdapter = new ChatAdapter();

        recyclerview.setAdapter(mAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        send_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = inputView.getText().toString();
                int type = ChatContract.ChatMessage.TYPE_SEND;

                switch (typeView.getCheckedRadioButtonId()) {
                    case R.id.radio_send:
                        type = ChatContract.ChatMessage.TYPE_SEND;
                        break;

                    case R.id.radio_receive:
                        type = ChatContract.ChatMessage.TYPE_RECEIVE;
                        break;
                }

                //싱글톤을 적용한 디비관리 클래스.//
                DBManager.getInstance().addMessage(user, type, message);

                //데이터베이스 내용을 갱신.//
                updateMessage();
            }
        });
    }

    private void updateMessage() {
        Cursor c = DBManager.getInstance().getChatMessage(user);

        mAdapter.changeCursor(c); //데이터베이스의 내용을 참조하고 있는 커서의 내용을 갱신.//
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateMessage();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAdapter.changeCursor(null);
    }
}
