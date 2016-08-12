package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestCode.ProfileRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.ProfileRequest;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    Handler mHandler;
    NetworkManager manager;
    private ProgressDialog pDialog;
    private Callback requestprofilecallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Toast.makeText(SplashActivity.this, "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();

            hidepDialog();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("splash data : ", response_data);

            //json 파싱//
            Gson gson = new Gson();

            ProfileRequestCode requestcode = gson.fromJson(response_data, ProfileRequestCode.class);

            if (requestcode.get_request_code() == 2) {
                //로그인이 되지 않았거나 가입이 되지 않아서 로그인 화면이 필요한 경우.//
                Log.d("Message : ", "Move LoginActivity");

                runOnUiThread(new Runnable() {
                    public void run() {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(SplashActivity.this, SampleLoginActivity.class));

                                hidepDialog();

                                finish();
                            }
                        }, 2000);
                    }
                });
            } else if (requestcode.get_request_code() == 1) {
                //로그인이 되었기에 바로 메인화면으로 이동.//
                //메인화면으로 이동 시 사용자의 정보를 같이 넘겨준다.//
                Log.d("Message : ", "Move MainActivity");

                ProfileRequest profile_request = gson.fromJson(response_data, ProfileRequest.class);

                final String user_email = profile_request.getResult().getEmail();
                final String user_name = profile_request.getResult().getUserName();
                final String user_id = "" + profile_request.getResult().getId();

                runOnUiThread(new Runnable() {
                    public void run() {
                        mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);

                                intent.putExtra("KEY_USER_EMAIL", user_email);
                                intent.putExtra("KEY_USER_NAME", user_name);
                                intent.putExtra("KEY_USER_ID", user_id);

                                startActivity(intent);

                                hidepDialog();

                                finish();
                            }
                        }, 2000);
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mHandler = new Handler(Looper.getMainLooper());

        //자동로그인 기능 구현. (/profile)//
        AutoLogin();
    }

    public void AutoLogin() {
        showpDialog();

        //로그인 관련 네트워크 설정.//
        //GET 방식 네트워크를 구성 (OkHttp3 설정 -> URL 설정 -> Request 설정 -> Callback구현)//
        /** OkHttp3 설정 (Cache, Cookie, etc) **/
        manager = NetworkManager.getInstance();

        OkHttpClient client = manager.getClient();

        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("profile"); //path지정.//

        //GET방식은 RequestBody는 필요없다.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestprofilecallback);
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
