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

import com.example.apple.sample_app.JSON_Data.RequestCode.SignUpRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestFail.SignUpRequestFail;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.SignUpRequest;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.data.PropertyManager;
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

public class SignUp_Activity extends AppCompatActivity {
    String user_registerid = "1234"; //1234로 지정.//

    EditText user_name_edit;
    EditText user_password_edit;
    EditText user_email_edit;

    Button enroll_button;
    NetworkManager manager;
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    private ProgressDialog pDialog;
    private Callback requestsignupcallback = new Callback() {
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

            SignUpRequestCode request_code = gson.fromJson(response_data, SignUpRequestCode.class);

            response_code = request_code.get_request_code();

            Log.d("result code", "" + response_code);

            //2가지 응답코드에 따른 분리.//
            if (response_code == 1) //유저등록에 성공한 경우.//
            {
                //성공한 경우에 따른 json 파싱.//
                SignUpRequest sign_request_info = gson.fromJson(response_data, SignUpRequest.class);

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
                SignUpRequestFail request_fail_info = gson.fromJson(response_data, SignUpRequestFail.class);

                Log.d("result : ", request_fail_info.getResult());

                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(SignUp_Activity.this, "이미 등록된 유저입니다.", Toast.LENGTH_SHORT).show();

                        hidepDialog();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singn_up);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        user_name_edit = (EditText) findViewById(R.id.edit_username);
        user_password_edit = (EditText) findViewById(R.id.edit_password);
        user_email_edit = (EditText) findViewById(R.id.edit_email);
        enroll_button = (Button) findViewById(R.id.btn_sign_up);

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

        enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버로 데이터를 전송한다. 전송방식은 POST를 따른다.//
                Login_Info_Send();
            }
        });
    }

    public void Login_Info_Send() {
        showpDialog();

        String user_name = user_name_edit.getText().toString();
        String user_email = user_email_edit.getText().toString();
        String user_password = user_password_edit.getText().toString();

        //POST 방식 네트워크를 구성 (OkHttp3 설정 -> URL 설정 -> RequestBody 정의 -> Request 설정 -> Callback구현)//
        /** OkHttp3 설정 (Cache, Cookie, etc) **/
        manager = NetworkManager.getInstance();

        OkHttpClient client = manager.getClient();

        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("signup"); //path지정.//

        /** RequestBody설정(POST / KEY-VALUE) **/
        RequestBody body = new FormBody.Builder()
                .add("username", user_name)
                .add("password", user_password)
                .add("email", user_email)
                .add("registrationId", user_registerid)
                .build(); //build()로 body를 생성.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestsignupcallback);
    }

    public void set_Data(final SignUpRequest signuprequest) {
        runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(SignUp_Activity.this, "등록되었습니다.", Toast.LENGTH_SHORT).show();

                String user_email = user_email_edit.getText().toString();
                String user_password = user_password_edit.getText().toString();
                String user_name = signuprequest.getResult().getUserName();
                String user_id = "" + signuprequest.getResult().getId();

                //프래퍼런스를 셋팅.//
                mPrefs = PreferenceManager.getDefaultSharedPreferences(SignUp_Activity.this);
                mEditor = mPrefs.edit();

                //프래퍼런스에 등록//
                PropertyManager.getInstance().setEmail(user_email);
                PropertyManager.getInstance().setPassword(user_password);
                PropertyManager.getInstance().setRegisterid(user_registerid);

                hidepDialog();

                finish();

                Intent intent = new Intent(SignUp_Activity.this, MainActivity.class);

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
