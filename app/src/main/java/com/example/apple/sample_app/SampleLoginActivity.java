package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestCode.SignInRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestFail.SignInRequestFail;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.SignInRequest;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.data.Manager_Data.PropertyManager;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SampleLoginActivity extends AppCompatActivity {
    Button sign_button;
    Button login_button;
    EditText input_email_edit;
    EditText input_password_edit;

    String user_email;
    String user_password;
    String user_registerid = "1234";
    NetworkManager manager;
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sign_button = (Button) findViewById(R.id.sign_button);
        login_button = (Button) findViewById(R.id.login_button_user);
        input_email_edit = (EditText) findViewById(R.id.input_email_edit);
        input_password_edit = (EditText) findViewById(R.id.input_password_edit);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        sign_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //회원가입 페이지로 이동.//
                Intent intent = new Intent(SampleLoginActivity.this, SignUp_Activity.class);

                startActivity(intent);
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
            private Callback requestsignincallback = new Callback() {
                @Override
                public void onFailure(Call call, IOException e) //접속 실패의 경우.//
                {
                    //네트워크 자체에서의 에러상황.//
                    Log.d("ERROR Message : ", e.getMessage());
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String response_data = response.body().string();

                    int response_code;

                    Log.d("json data : ", response_data);

                    //우선적으로 응답코드를 얻기 위해서 초기 파싱.//
                    Gson gson = new Gson();

                    SignInRequestCode request_code = gson.fromJson(response_data, SignInRequestCode.class);

                    response_code = request_code.get_request_code();

                    Log.d("result code", "" + response_code);

                    //2가지 응답코드에 따른 분리.//
                    if (response_code == 1) //유저등록에 성공한 경우.//
                    {
                        //성공한 경우에 따른 json 파싱.//
                        SignInRequest sign_request_info = gson.fromJson(response_data, SignInRequest.class);

                        String user_name = sign_request_info.getResult().getUserName();
                        String user_email = sign_request_info.getResult().getEmail();
                        String user_id = "" + sign_request_info.getResult().getId();

                        Log.d("user name : ", user_name);
                        Log.d("user email : ", user_email);
                        Log.d("user id : ", user_id);

                        //필요한 값을 셋팅. 기본적으로 콜백 메소드 안에서는 UI스레드의 작업 불가.//
                        set_Data(sign_request_info);

                    } else if (response_code == 2) //유저등록에 실패한 경우.//
                    {
                        SignInRequestFail request_fail_info = gson.fromJson(response_data, SignInRequestFail.class);

                        Log.d("result : ", request_fail_info.getResult());

                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(SampleLoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();

                                hidepDialog();
                            }
                        });
                    }
                }
            };

            @Override
            public void onClick(View view) {
                showpDialog();

                user_email = input_email_edit.getText().toString();
                user_password = input_password_edit.getText().toString();

                //로그인 관련 네트워크 설정.//
                //POST 방식 네트워크를 구성 (OkHttp3 설정 -> URL 설정 -> RequestBody 정의 -> Request 설정 -> Callback구현)//
                /** OkHttp3 설정 (Cache, Cookie, etc) **/
                /** OkHttp3 설정 (Cache, Cookie, etc) **/
                manager = NetworkManager.getInstance(); //SingleTone디자인 패턴으로 해야지 해당 쿠키가 하나만 나온다.//

                OkHttpClient client = manager.getClient();

                /** URL 설정 **/
                HttpUrl.Builder builder = new HttpUrl.Builder();

                builder.scheme("https"); //스킴정의(Http / Https)
                builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
                builder.addPathSegment("signin"); //path지정.//

                /** RequestBody설정(POST / KEY-VALUE) **/
                RequestBody body = new FormBody.Builder()
                        .add("email", user_email)
                        .add("password", user_password)
                        .add("registrationId", user_registerid)
                        .build(); //build()로 body를 생성.//

                /** Request 설정 **/
                Request request = new Request.Builder()
                        .url(builder.build())
                        .post(body)
                        .tag(this)
                        .build();

                /** 비동기 방식(enqueue)으로 Callback 구현 **/
                client.newCall(request).enqueue(requestsignincallback);
            }
        });

        //프래퍼런스를 셋팅.//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        //정보 가져오기//
        String user_email = PropertyManager.getInstance().getEmail();
        String user_password = PropertyManager.getInstance().getPassword();

        input_email_edit.setText(user_email);
        input_password_edit.setText(user_password);
    }

    public void set_Data(final SignInRequest sign_request_info) {
        runOnUiThread(new Runnable() {
            public void run() {
                String user_name = sign_request_info.getResult().getUserName();

                //공유 프래퍼런스에 저장.//
                //프래퍼런스를 셋팅.//
                mPrefs = PreferenceManager.getDefaultSharedPreferences(SampleLoginActivity.this);
                mEditor = mPrefs.edit();

                String user_email = sign_request_info.getResult().getEmail();
                String user_password = input_password_edit.getText().toString();
                String user_id = "" + sign_request_info.getResult().getId();

                //프래퍼런스에 등록//
                PropertyManager.getInstance().setEmail(user_email);
                PropertyManager.getInstance().setPassword(user_password);
                PropertyManager.getInstance().setRegisterid(user_registerid);

                Toast.makeText(SampleLoginActivity.this, user_name + "로그인 성공", Toast.LENGTH_SHORT).show();

                hidepDialog();

                finish();

                Intent intent = new Intent(SampleLoginActivity.this, MainTabActivity.class);

                intent.putExtra("KEY_USER_EMAIL", user_email);
                intent.putExtra("KEY_USER_NAME", user_name);
                intent.putExtra("KEY_USER_ID", user_id);

                startActivity(intent);
            }
        });
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
