package com.example.apple.sample_app.DataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.apple.sample_app.data.ChatContract;
import com.example.apple.sample_app.data.MyApplication;
import com.example.apple.sample_app.data.User;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by apple on 2016. 8. 11..
 */

/**
 * SQLiteOpenHelper는 데이터베이스 관리를 체계적으로 해주기 위한 클래스.
 * 데이터베이스의 생성과 업데이트, 그 외의 쿼리기능을 수행.
 * onCreate() -> 처음 데이터베이스를 만들때 수행되는 것으로 테이블을 생성.
 * onUpgrade() -> 데이터베이스의 버전이 업데이트 되었을 때 수행
 */
public class DBManager extends SQLiteOpenHelper {
    private static final String DB_NAME = "chat_db";
    private static final int DB_VERSION = 1;
    private static DBManager instance;
    ContentValues values = new ContentValues();
    Map<Long, Long> resolveUserId = new HashMap<>();

    private DBManager() {
        super(MyApplication.getContext(), DB_NAME, null, DB_VERSION);
    }

    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    //싱글톤 패턴 적용.//
    public static DBManager getInstance() {
        if (instance == null) {
            instance = new DBManager();
        }
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //테이블 2개를 생성.//
        String sql = "CREATE TABLE " + ChatContract.ChatUser.TABLE + "(" +
                ChatContract.ChatUser._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChatContract.ChatUser.COLUMN_SERVER_ID + " INTEGER," +
                ChatContract.ChatUser.COLUMN_NAME + " TEXT," +
                ChatContract.ChatUser.COLUMN_EMAIL + " TEXT NOT NULL," +
                ChatContract.ChatUser.COLUMN_LAST_MESSAGE_ID + " INTEGER);";
        sqLiteDatabase.execSQL(sql); //해당 sql문을 실행.//

        sql = "CREATE TABLE " + ChatContract.ChatMessage.TABLE + "(" +
                ChatContract.ChatMessage._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                ChatContract.ChatMessage.COLUMN_USER_ID + " INTEGER," +
                ChatContract.ChatMessage.COLUMN_TYPE + " INTEGER," +
                ChatContract.ChatMessage.COLUMN_MESSAGE + " TEXT," +
                ChatContract.ChatMessage.COLUMN_CREATED + " INTEGER);";
        sqLiteDatabase.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public long getUserId(long serverId) //사용자 id를 불러오는 작업.//
    {
        String selection = ChatContract.ChatUser.COLUMN_SERVER_ID + " = ?";
        String[] args = {"" + serverId};
        String[] columns = {ChatContract.ChatUser._ID};
        SQLiteDatabase db = getReadableDatabase(); //읽기가능 모드로 전환.//

        //일반 SELECT로 수행하기 위해서 query()를 수행. 다른 DML들은 해당 name으로 설정.//
        Cursor c = db.query(ChatContract.ChatUser.TABLE, columns, selection, args, null, null, null);

        try {
            if (c.moveToNext()) //현재의 행에서 다음 행으로 커서를 이동.//
            {
                //getColumnIndex()를 이용해서 해당 커서가 위치한 곳의 정보를 가져온다.//
                long id = c.getLong(c.getColumnIndex(ChatContract.ChatUser._ID));

                return id;
            }
        } finally {
            c.close();
        }
        return -1;
    }

    public long addUser(User user) //INSERT로 유저를 추가하는 작업.//
    {
        if (getUserId(user.getId()) == -1) {
            SQLiteDatabase db = getWritableDatabase(); //추가하는 것이니 데이터베이스를 쓰기모드로 개방.//

            values.clear();

            values.put(ChatContract.ChatUser.COLUMN_SERVER_ID, user.getId());
            values.put(ChatContract.ChatUser.COLUMN_NAME, user.getUserName());
            values.put(ChatContract.ChatUser.COLUMN_EMAIL, user.getEmail());

            return db.insert(ChatContract.ChatUser.TABLE, null, values);
        }

        throw new IllegalArgumentException("aleady user added"); //insert에러시 예외.//
    }

    public long addMessage(User user, int type, String message) {
        Long uid = resolveUserId.get(user.getId());

        if (uid == null) {
            long id = getUserId(user.getId());

            if (id == -1) {
                id = addUser(user);
            }

            resolveUserId.put(user.getId(), id);

            uid = id;
        }

        SQLiteDatabase db = getWritableDatabase();

        values.clear();

        values.put(ChatContract.ChatMessage.COLUMN_USER_ID, uid);
        values.put(ChatContract.ChatMessage.COLUMN_TYPE, type);
        values.put(ChatContract.ChatMessage.COLUMN_MESSAGE, message);

        long current = System.currentTimeMillis();
        values.put(ChatContract.ChatMessage.COLUMN_CREATED, current);

        try {
            db.beginTransaction(); //트랜잭션을 적용.//
            long mid = db.insert(ChatContract.ChatMessage.TABLE, null, values);

            values.clear();
            values.put(ChatContract.ChatUser.COLUMN_LAST_MESSAGE_ID, mid);
            String selection = ChatContract.ChatUser._ID + " = ?";
            String[] args = {"" + uid};

            db.update(ChatContract.ChatUser.TABLE, values, selection, args);
            db.setTransactionSuccessful();

            return mid;
        } finally {
            db.endTransaction(); //트랜잭션을 시작했으니 finally문으로 종료를 항상 보장.//
        }
    }

    public Cursor getChatUser() {
        String table = ChatContract.ChatUser.TABLE + " INNER JOIN " +
                ChatContract.ChatMessage.TABLE + " ON " +
                ChatContract.ChatUser.TABLE + "." + ChatContract.ChatUser.COLUMN_LAST_MESSAGE_ID + " = " +
                ChatContract.ChatMessage.TABLE + "." + ChatContract.ChatMessage._ID;

        String[] columns = {ChatContract.ChatUser.TABLE + "." + ChatContract.ChatUser._ID,
                ChatContract.ChatUser.COLUMN_SERVER_ID,
                ChatContract.ChatUser.COLUMN_EMAIL,
                ChatContract.ChatUser.COLUMN_NAME,
                ChatContract.ChatMessage.COLUMN_MESSAGE};

        String sort = ChatContract.ChatUser.COLUMN_NAME + " COLLATE LOCALIZED ASC";

        SQLiteDatabase db = getReadableDatabase();

        return db.query(table, columns, null, null, null, null, sort);
    }

    public Cursor getChatMessage(User user) {
        long userid = -1;
        Long uid = resolveUserId.get(user.getId());
        if (uid == null) {
            long id = getUserId(user.getId());
            if (id != -1) {
                resolveUserId.put(user.getId(), id);
                userid = id;
            }
        } else {
            userid = uid;
        }

        String[] columns = {ChatContract.ChatMessage._ID,
                ChatContract.ChatMessage.COLUMN_TYPE,
                ChatContract.ChatMessage.COLUMN_MESSAGE,
                ChatContract.ChatMessage.COLUMN_CREATED};

        String selection = ChatContract.ChatMessage.COLUMN_USER_ID + " = ?";

        String[] args = {"" + userid};

        String sort = ChatContract.ChatMessage.COLUMN_CREATED + " ASC";
        SQLiteDatabase db = getReadableDatabase();

        return db.query(ChatContract.ChatMessage.TABLE, columns, selection, args, null, null, sort);
    }

}
