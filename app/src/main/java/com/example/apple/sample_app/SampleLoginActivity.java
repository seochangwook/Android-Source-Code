package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
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

import com.example.apple.sample_app.JSON_Data.RequestCode.FacebookRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestCode.SignInRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestFail.SignInRequestFail;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.FacebookSigninRequest_2;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.SignInRequest;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.data.Manager_Data.PropertyManager;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Arrays;

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
    Handler mHandler;
    /**
     * Facebook 로그인 관련
     **/
    //버튼 커스텀//
    LoginManager mLoginManager;
    Button facebook_login_button;
    AccessTokenTracker tracker; //로그인 정보 추적//
    private ProgressDialog pDialog;
    private CallbackManager callbackManager; //세션연결 콜백관리자.//
    private Callback requestfacebooksignincallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Log.d("facebook login data : ", response_data);

            //회원가입 유무를 판단가능.//
            Gson gson = new Gson();

            FacebookRequestCode result_code = gson.fromJson(response_data, FacebookRequestCode.class);

            int code = result_code.get_request_code();

            if (code == 1) //로그인은 되어 있지 않으나 회원가입은 된 경우//
            {
                Log.d("process : ", "code 1");
            } else if (code == 2) //잘못된 토큰값.//
            {
                Log.d("process : ", "invalid token");
            } else if (code == 3) //로그인도 되어 있지 않고 회원가입도 안된 경우.//
            {
                Log.d("process : ", "code 3");

                //해당 구문으로 파싱.//
                //가입을 할려면 이름과 이메일 주소가 필요한데 해당 파싱 후 정보를 회원가입 페이지로 전달.//
                FacebookSigninRequest_2 facebookSigninRequest_2 = gson.fromJson(response_data, FacebookSigninRequest_2.class);

                final String user_name = facebookSigninRequest_2.getResult().getName();
                final String user_email = facebookSigninRequest_2.getResult().getEmail();
                final String user_id = facebookSigninRequest_2.getResult().getId();

                //회원가입을 바로 시도.//
                runOnUiThread(new Runnable() {
                    public void run() {
                        //토큰값을 전달하고 사용자 이름정보와 프로필 이미지 경로, id를 받는다.//
                        //id를 전달받으면 프레퍼런스에 저장.(로그인이 안되었다는 가정하에 진행)//


                        Intent intent = new Intent(SampleLoginActivity.this, MainTabActivity.class);

                        //필요한 인자 전달.//
                        //intent.putExtra("KEY_USER_NAME", user_name);
                        //intent.putExtra("KEY_USER_EMAIL", user_email);
                        //서버로 부터 전송받아온 id를 전송//
                        intent.putExtra("KEY_USER_ID", user_id);

                        startActivity(intent);

                        finish();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext()); //초기 facebook연동을 하기 위해서 초기화//
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_sample_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        sign_button = (Button) findViewById(R.id.sign_button);
        login_button = (Button) findViewById(R.id.login_button_user);
        input_email_edit = (EditText) findViewById(R.id.input_email_edit);
        input_password_edit = (EditText) findViewById(R.id.input_password_edit);
        facebook_login_button = (Button) findViewById(R.id.facebook_login_button_custom);

        mLoginManager = LoginManager.getInstance();

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

        callbackManager = CallbackManager.Factory.create(); //onActivityResult설정.//

        facebook_login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin()) {
                    logoutFacebook();
                } else {
                    loginFacebook();
                }

            }
        });
    }

    public void onStart() {
        super.onStart();

        /** Facebook AccessTokenTracker 관련 **/
        if (tracker == null) {
            tracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                    setButtonLabel();
                }
            };
        } else {
            tracker.startTracking(); //추적을 시작.//
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        //트래킹 종료.//
        tracker.stopTracking();
    }

    /**
     * Facebook 관련 CustomLogin
     **/

    private void logoutFacebook() {
        mLoginManager.logOut(); //로그아웃.//
    }

    private void loginFacebook() {
        mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);

        //콜백등록.//
        mLoginManager.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Access Token값을 가져온다.//
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                String token = accessToken.getToken();

                Log.d("token : ", token);

                String user_id = accessToken.getUserId();

                //해당 토큰값을 서버로 전송한다.//
                trans_facebook_data(user_registerid, token);

                //Toast.makeText(SampleLoginActivity.this, "Token : " + token + "/ user id : " + user_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //기존 제공해주는 로그인 버튼으로도 가능.//
        mLoginManager.logInWithReadPermissions(SampleLoginActivity.this, Arrays.asList("email")); //이메일 획득 권한//
    }

    public void trans_facebook_data(String user_registerid, String token) {
        /** HttpUrl 설정 **/
        manager = NetworkManager.getInstance();

        OkHttpClient client = manager.getClient();

        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("facebooksignin"); //path지정.//

        /** RequestBody 설정 **/
        RequestBody body = new FormBody.Builder()
                .add("access_token", token)
                .add("registrationId", user_registerid)
                .build();

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body)
                .tag(this)
                .build();

        client.newCall(request).enqueue(requestfacebooksignincallback);
    }

    private boolean isLogin() {
        AccessToken token = AccessToken.getCurrentAccessToken();

        return token != null; //로그인 구분.//
    }

    private void setButtonLabel() {
        if (isLogin()) {
            facebook_login_button.setText("logout");
        } else {
            facebook_login_button.setText("login");
        }
    }

    //인증에 대한 결과를 받는다.//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Facebook 로그인 관련 **/
        //등록이 되어있어야지 정상적으로 onSuccess에서 정보를 받아온다.//
        callbackManager.onActivityResult(requestCode, resultCode, data); //

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());
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
