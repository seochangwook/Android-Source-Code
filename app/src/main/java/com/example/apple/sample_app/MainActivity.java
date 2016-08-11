package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestCode.LogoutRequestCode;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    String user_email;
    String user_name;
    String user_id;
    NetworkManager manager;
    private ProgressDialog pDialog;
    private Callback requestlogoutcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Toast.makeText(MainActivity.this, "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();

            hidepDialog();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("splash data : ", response_data);

            //json 파싱//
            Gson gson = new Gson();

            LogoutRequestCode logout_request = gson.fromJson(response_data, LogoutRequestCode.class);

            int result_code = logout_request.get_request_code();

            if (result_code == 1) {
                //정상적인 로그아웃//
                runOnUiThread(new Runnable() {
                    public void run() {
                        startActivity(new Intent(MainActivity.this, SampleLoginActivity.class));

                        hidepDialog();

                        finish();
                    }
                });
            } else if (result_code == 2) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();

                        hidepDialog();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent intent = getIntent();

        user_email = intent.getStringExtra("KEY_USER_EMAIL");
        user_name = intent.getStringExtra("KEY_USER_NAME");
        user_id = intent.getStringExtra("KEY_USER_ID");

        Log.d("main / user email : ", user_email);
        Log.d("main / user name : ", user_name);
        Log.d("main / user id : ", user_id);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.logout_button) {
            logout();
        }

        return super.onOptionsItemSelected(item);
    }

    public void logout() {
        showpDialog();

        //GET 방식 네트워크를 구성 (OkHttp3 설정 -> URL 설정 -> Request 설정 -> Callback구현)//
        /** OkHttp3 설정 (Cache, Cookie, etc) **/
        manager = NetworkManager.getInstance();

        OkHttpClient client = manager.getClient();

        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("logout"); //path지정.//

        //GET방식은 RequestBody는 필요없다.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestlogoutcallback);
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
