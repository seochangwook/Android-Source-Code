package com.example.apple.sample_app;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestCode.ProfileRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.ProfileRequest;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.data.Manager_Data.PropertyManager;
import com.example.apple.sample_app.gcm.RegistrationIntentService;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class SplashActivity extends AppCompatActivity {
    /**
     * GCM관련 변수
     **/
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    //각 SNS마다 로그인이 되어 있지 않은 경우.//
    private static int FACEBOOK_LOGIN_CONDITION = 0; //0이면 로그인 성공의 경우. -1이면 실패인 경우.//
    Handler mHandler;
    NetworkManager manager;
    SharedPreferences mPrefs; //공유 프래퍼런스 정의.(서버가 토큰 비교 후 반환해 준 id를 기존에 저장되어 있는 id값과 비교하기 위해)//
    SharedPreferences.Editor mEditor; //프래퍼런스 에디터 정의//
    /**
     * Facebook Login 관련 변수
     **/
    LoginManager mLoginManager; //로그인을 관리해주는 로그인 매니저.//
    AccessTokenTracker tracker; //로그인 정보 추적//
    private ProgressDialog pDialog;
    private CallbackManager callbackManager; //세션연결 콜백관리자.//
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private Callback requestprofilecallback = new Callback()
    {
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
                String registoken = PropertyManager.getInstance().getRegistrationToken();

                Log.d("register token : ", registoken);

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
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        FacebookSdk.sdkInitialize(getApplicationContext()); //초기 facebook연동을 하기 위해서 초기화//
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_splash);

        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                doRealStart();
            }
        };

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mHandler = new Handler(Looper.getMainLooper());

        //프래퍼런스를 셋팅.//
        mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        mEditor = mPrefs.edit();

        /** Facebook 로그인 관련 옵션 설정 **/
        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create(); //onActivityResult설정.//

        setUpIfNeeded();
        //자동로그인 기능 구현. (/profile)//
        //AutoLogin();

        //SNS자동 로그인(구글, 페이스북, 트위터)//
        //Auto_SNS_Login();
    }

    @Override
    protected void onResume() {
        /** GCM **/
        super.onResume();
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(RegistrationIntentService.REGISTRATION_COMPLETE)); //새로 생성된 토큰값을 받는다.//
    }

    @Override
    protected void onPause() {
        /** GCM **/
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }

    /**
     * GCM
     **/
    private void doRealStart() {
        // activity start...
        Auto_SNS_Login(); //인증키를 얻은 후
    }

    /**
     * GCM
     **/
    private void setUpIfNeeded() {
        if (checkPlayServices()) //register토큰을 발급받을 수 있는 상태인지 확인.//
        {
            String regId = PropertyManager.getInstance().getRegistrationToken(); //저장되어 있는지 확인.(이미 발급 유무 확인)//

            if (!regId.equals("")) {
                doRealStart(); //서비스 구동(로그인 과정 진행)//
            } else {
                //발급 받을 수 없을 경우 발급할 수 있는 서비스 구동//
                Intent intent = new Intent(this, RegistrationIntentService.class);
                startService(intent);
            }
        }
    }

    /**
     * GCM
     **/
    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();

        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) //에러 다이얼로그 띄움//
            {
                Dialog dialog = apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST);
                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        finish();
                    }
                });

                dialog.show();
            } else {
                finish();
            }

            return false;
        }

        return true;
    }

    public void Auto_SNS_Login() {
        showpDialog();

        /** FaceBook Login 관련 **/
        is_Facebook_Login();

        hidepDialog();

        //로그인 상태 정보에 따른 조건 분기//
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Facebook 로그인 관련 **/
        //등록이 되어있어야지 정상적으로 onSuccess에서 정보를 받아온다.//
        callbackManager.onActivityResult(requestCode, resultCode, data); //

        /** GCM **/
        if (requestCode == PLAY_SERVICES_RESOLUTION_REQUEST && resultCode == Activity.RESULT_OK) {
            setUpIfNeeded();

            return;
        }
    }

    public void is_Facebook_Login() {
        mLoginManager.setDefaultAudience(DefaultAudience.FRIENDS);
        mLoginManager.setLoginBehavior(LoginBehavior.NATIVE_WITH_FALLBACK);
        /** 시나리오 1 : 우선적으로 페이스북 서버에서 토큰값을 획득한다. **/
        final AccessToken accessToken = AccessToken.getCurrentAccessToken();

        //처음 스플래시 화면 시 로그인이 되어 있지 않으면 토큰값은 null이 된다.//
        //로그인 버튼을 이용하여 로그인 후 다시 스플레시 적용 시 토큰값이 정상적으로 넘어온다.//
        /** 시나리오 2 : 토큰의 값을 비교 **/
        if (accessToken == null) {
            Log.d("splash message : ", "Token ERROR");

            //토큰이 없기에 로그인 화면으로 이동.//
            runOnUiThread(new Runnable() {
                public void run() {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mLoginManager.logOut(); //우선적으로 깔끔하게 로그아웃을 한다.//

                            Intent intent = new Intent(SplashActivity.this, SampleLoginActivity.class);

                            startActivity(intent);

                            hidepDialog();

                            finish();
                        }
                    }, 2000);
                }
            });
        } else {
            final String token = accessToken.getToken(); //토큰값을 받는다.//

            //토큰값을 서버로 전송하여 페이스북 id를 얻어와서 기존 프레퍼런스에 저장된 id와 비교..//
            //비교 후 일치하면 메인으로 이동. 일치하지 않으면 로그인 화면으로 이동.//

            runOnUiThread(new Runnable() {
                public void run() {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Intent intent = new Intent(SplashActivity.this, MainTabActivity.class);

                            //필요한 인자 전달.//
                            //intent.putExtra("KEY_USER_NAME", user_name);
                            //intent.putExtra("KEY_USER_EMAIL", user_email);
                            //서버로 부터 전송받아온 id를 전송//
                            intent.putExtra("KEY_USER_ID", accessToken.getUserId());

                            startActivity(intent);

                            hidepDialog();

                            finish();
                        }
                    }, 2000);
                }
            });
        }
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
