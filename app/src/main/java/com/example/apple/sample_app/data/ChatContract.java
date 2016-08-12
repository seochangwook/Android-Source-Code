package com.example.apple.sample_app.data;

import android.provider.BaseColumns;

/**
 * Created by apple on 2016. 8. 11..
 */
public class ChatContract {
    //여러 호출되는 상수들을 정의(테이블의 컬럼명 등)//
    public interface ChatUser extends BaseColumns {
        String TABLE = "chatuser";
        String COLUMN_SERVER_ID = "sid";
        String COLUMN_NAME = "name";
        String COLUMN_EMAIL = "email";
        String COLUMN_LAST_MESSAGE_ID = "lastid";
    }

    public interface ChatMessage extends BaseColumns {
        int TYPE_SEND = 0;
        int TYPE_RECEIVE = 1;
        int TYPE_DATE = 2;

        String TABLE = "chatmessage";
        String COLUMN_USER_ID = "uid";
        String COLUMN_TYPE = "type";
        String COLUMN_MESSAGE = "message";
        String COLUMN_CREATED = "created";
    }
}
