package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.DefaultAudience;
import com.facebook.login.LoginBehavior;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.models.User;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import io.fabric.sdk.android.Fabric;

public class SNS_Login_Activity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    /**
     * Twitter 로그인 관련
     **/
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "68QkLNjEDpFWQqi4Su0eRkOtV";
    private static final String TWITTER_SECRET = "pC0gMw69kGAvk8AApObxe6XAjzNKPBhCj9ABuAYVDsj5cs5nuT";
    /**
     * Google 로그인 관련
     **/
    private static final String TAG = "SNS_Login_Activity";
    private static final int RC_SIGN_IN = 9001;
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME_FACEBOOK = "user_name_facebook";
    private static final String KEY_USER_PROFILE_URL = "user_profile_url";
    private static final String KEY_USER_PROFILE_URL_TWITTER = "user_profile_url";
    private static final String KEY_USER_NAME_GOOGLE = "user_name_google";
    private static final String KEY_USER_NAME_TWITTER = "user_name_twitter";
    private static final String KEY_SNS_CATEGORY = "sns_category";
    TwitterSession session; //트위터 세션.//
    TwitterLoginButton twitter_login_button;
    String username;
    String profileImage;
    Twitter_ImageTask get_profile_image_task_twitter; //계정 이미지 불러오기 작업//
    Google_ImageTask get_profile_image_task_google; //계정 이미지 불러오기 작업//
    SignInButton sign_in_button;
    /**
     * Facebook 로그인 관련
     **/
    LoginButton loginbutton; //페이스북 로그인 버튼//
    String id, name, profile_link;
    ImageTask get_profile_image_task_facebook; //계정 이미지 불러오기 작업//
    String profile_img_url, user_name, user_email;
    Button sign_out_button;
    ImageView profile_image;
    int what_sns_click; //1이면 페이스북, 2이면 구글//

    //버튼 커스텀//
    LoginManager mLoginManager;
    Button facebook_login_button;
    AccessTokenTracker tracker; //로그인 정보 추적//
    /**
     * 메뉴 관련
     **/
    //private ResideMenu resideMenu;

    EditText token_edit;
    private GoogleApiClient mGoogleApiClient;
    private CallbackManager callbackManager; //세션연결 콜백관리자.//
    private TextView mStatusTextView;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /** 트위터, 페이스북에 대한 인증은 setContentView()이전에 이루어져야 한다. **/
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        FacebookSdk.sdkInitialize(getApplicationContext()); //초기 facebook연동을 하기 위해서 초기화//
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_sns_login);

        mStatusTextView = (TextView) findViewById(R.id.status);
        sign_in_button = (SignInButton) findViewById(R.id.google_sign_in_button);
        sign_out_button = (Button) findViewById(R.id.sign_out_button);
        //disconnect_button = (Button)findViewById(R.id.disconnect_button);
        profile_image = (ImageView) findViewById(R.id.profile_imageView);
        loginbutton = (LoginButton) findViewById(R.id.facebook_login_button);
        twitter_login_button = (TwitterLoginButton) findViewById(R.id.twitter_login_button);
        facebook_login_button = (Button) findViewById(R.id.facebook_login_button_custom);

        mLoginManager = LoginManager.getInstance();

        /** Reside Menu 구성 **/
        //리사이드 메뉴 구성//
        /*resideMenu = new ResideMenu(this); //액티비티에 겹쳐서 뿌려지므로 액티비티의 자원을 얻는다.//
        resideMenu.setBackground(R.drawable.menu_background);
        resideMenu.attachToActivity(this);
        resideMenu.setScaleValue(0.5f);

        resideMenu.setSwipeDirectionDisable(ResideMenu.DIRECTION_RIGHT); //오른쪽 스크롤을 막는다.//

        //메뉴아이템 등록//
        ResideMenuItem item_home = new ResideMenuItem(this, R.drawable.icon_home, "Home");
        resideMenu.addMenuItem(item_home, ResideMenu.DIRECTION_LEFT); //왼쪽에서 메뉴가 등장.//

        ResideMenuItem item_profile = new ResideMenuItem(this, R.drawable.icon_profile, "Profile");
        resideMenu.addMenuItem(item_profile, ResideMenu.DIRECTION_LEFT); //왼쪽에서 메뉴가 등장.//

        ResideMenuItem item_calendar = new ResideMenuItem(this, R.drawable.icon_calendar, "Calendar");
        resideMenu.addMenuItem(item_calendar, ResideMenu.DIRECTION_LEFT); //왼쪽에서 메뉴가 등장.//

        ResideMenuItem item_settings = new ResideMenuItem(this, R.drawable.icon_settings, "Settings");
        resideMenu.addMenuItem(item_settings, ResideMenu.DIRECTION_LEFT); //왼쪽에서 메뉴가 등장.//

        //이벤트 처리.//
        item_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SNS_Login_Activity.this, "홉 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        item_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SNS_Login_Activity.this, "나의 프로필 화면으로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(SNS_Login_Activity.this, My_Info_Activity.class);

                //페이스북, 구글서 받아온 정보를 넘긴다. 객체를 넘길 시 Parceable사용.//
                if (what_sns_click == 1) {
                    intent.putExtra(KEY_USER_ID, id);
                    intent.putExtra(KEY_USER_NAME_FACEBOOK, name);
                    intent.putExtra(KEY_SNS_CATEGORY, "1");
                } else if (what_sns_click == 2) {
                    intent.putExtra(KEY_USER_PROFILE_URL, profile_img_url);
                    intent.putExtra(KEY_USER_NAME_GOOGLE, user_name);
                    intent.putExtra(KEY_SNS_CATEGORY, "2");
                } else if (what_sns_click == 3) {
                    intent.putExtra(KEY_USER_PROFILE_URL_TWITTER, profileImage);
                    intent.putExtra(KEY_USER_NAME_TWITTER, username);
                    intent.putExtra(KEY_SNS_CATEGORY, "3");
                }

                startActivity(intent);

                resideMenu.closeMenu(); //메뉴를 다시 닫는다.//
            }
        });

        item_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SNS_Login_Activity.this, "달력 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        item_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(SNS_Login_Activity.this, "설정 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });*/

        /** Google 로그인 과정 **/
        //[START configure_signin]
        //Configure sign-in to request the user's ID, email address, and basic profile.
        //ID and basic profile are included in DEFAULT_SIGN_IN
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        //[END configure_signin]

        //연결과정의 핵심.//
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        sign_in_button.setSize(SignInButton.SIZE_STANDARD);
        sign_in_button.setScopes(gso.getScopeArray());

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

                what_sns_click = 2;
            }
        });

        sign_out_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        /*disconnect_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                revokeAccess();
            }
        });*/

        /** Facebook 로그인 과정 **/

        printKeyHash();

        callbackManager = CallbackManager.Factory.create(); //onActivityResult설정.//
        //loginbutton.setFragment("fragment name"); //프래그먼트 시 적용.//
        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //Volley방식으로 JSON파싱//
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Access Token값을 가져온다.//
                AccessToken accessToken = AccessToken.getCurrentAccessToken();

                String token = accessToken.getToken();
                String user_id = accessToken.getUserId();

                //해당 토큰값을 서버로 전송한다.//

                Toast.makeText(SNS_Login_Activity.this, "Token : " + token + "/ user id : " + user_id, Toast.LENGTH_SHORT).show();

                final GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Profile profile = Profile.getCurrentProfile(); //현재의 프로필 정보를 받아온다.//

                                    //사용자 인증정보를 가져온다. 기본적인 JSON정보와 Profile을 이용.//
                                    //Exception방지로 opt로 불러온다.//
                                    id = object.optString("id");//페이스북 아이디값
                                    name = object.optString("name");//페이스북 이름
                                    profile_link = "" + profile.getLinkUri(); //프로파일을 이용해서 사용자주소 링크를 가져온다.//

                                    if (name == null) {
                                        mStatusTextView.setText("null");
                                    } else {
                                        mStatusTextView.setText(name);
                                    }

                                    what_sns_click = 1;

                                    //id를 넘겨 이미지의 토큰값으로 활용//
                                    set_user_image(id);

                                } catch (FacebookException e) {
                                    Toast.makeText(SNS_Login_Activity.this, "사용자 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
                                }

                                // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다//
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {
                Toast.makeText(SNS_Login_Activity.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(SNS_Login_Activity.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

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

        setButtonLabel(); //버튼의 글씨변경.//

        /** Twitter 로그인 관련 **/
        twitter_login_button.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitter_login(result);

                what_sns_click = 3;
            }

            @Override
            public void failure(TwitterException exception) {
                Log.d("TwitterKit", "Login with Twitter failure", exception);
            }
        });
    }

    /*@Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //return resideMenu.dispatchTouchEvent(ev); //모션이벤트 등록//
    }*/

    /**
     * Google 로그인 캐쉬연결(자동)
     **/
    //@Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);

        if (opr.isDone()) {
            Log.d(TAG, "Got cached sign-in");

            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }

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

    //인증에 대한 결과를 받는다.//
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /** Google 로그인 관련 **/
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            handleSignInResult(result);
        }

        /** Facebook 로그인 관련 **/
        //등록이 되어있어야지 정상적으로 onSuccess에서 정보를 받아온다.//
        callbackManager.onActivityResult(requestCode, resultCode, data); //

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());

        /** Twitter 로그인 관련 **/
        twitter_login_button.onActivityResult(requestCode, resultCode, data);
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult: " + result.isSuccess());

        if (result.isSuccess()) //성공.//
        {
            GoogleSignInAccount acct = result.getSignInAccount();
            mStatusTextView.setText(acct.getDisplayName());

            user_name = mStatusTextView.getText().toString();

            profile_img_url = "" + acct.getPhotoUrl(); //구글계정의 프로필 이미지 경로를 얻는다.//

            updateUI(true);
        } else //실패.//
        {
            updateUI(false);
        }
    }

    private void signIn()  //로그인 시도.//
    {
        //인증방식은 Intent로 한다.//
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);

        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() //인증해제/
    {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                updateUI(false);
            }
        });
    }

    /*private void revokeAccess() {
        Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        updateUI(false);
                        // [END_EXCLUDE]
                    }
                });
    }*/
    // [END revokeAccess]

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean signedIn) {
        if (signedIn) //로그인의 경우.//
        {
            //findViewById(R.id.sign_in_button).setVisibility(View.GONE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
            loginbutton.setVisibility(View.GONE);
            sign_in_button.setVisibility(View.GONE);
            twitter_login_button.setVisibility(View.GONE);
            sign_out_button.setVisibility(View.VISIBLE);

            //이미지를 로딩.//
            get_profile_image_task_google = new Google_ImageTask(profile_img_url);
            get_profile_image_task_google.execute(); //스레드 작업 실행//
        } else //로그아웃 경우.//
        {
            mStatusTextView.setText(R.string.signed_out);

            loginbutton.setVisibility(View.VISIBLE);
            sign_in_button.setVisibility(View.VISIBLE);
            twitter_login_button.setVisibility(View.VISIBLE);
            sign_out_button.setVisibility(View.GONE);

            //findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
            //findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);

            profile_image.setImageResource(R.drawable.facebook_people_image); //디폴트 이미지.//
        }
    }

    /**
     * Twitter 로그인 관련
     **/
    public void twitter_login(Result<TwitterSession> result) {
        session = result.data;

        username = session.getUserName(); //이름 가져오기.//

        Twitter.getApiClient(session).getAccountService()
                .verifyCredentials(true, false, new Callback<User>() {
                    @Override
                    public void success(Result<User> userResult) {
                        User user = userResult.data;

                        //Getting the profile image url//
                        profileImage = user.profileImageUrl.replace("_normal", "");

                        Log.d("user id : ", username);
                        Log.d("user url : ", profileImage);

                        mStatusTextView.setText(username);

                        get_profile_image_task_twitter = new Twitter_ImageTask(profileImage);
                        get_profile_image_task_twitter.execute(); //스레드 작업 실행//
                    }

                    @Override
                    public void failure(TwitterException exception) {

                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login_activity_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.home_menu) {
            //resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        } else if (id == R.id.google_login) {
            Intent intent = new Intent(SNS_Login_Activity.this, LoginActivity.class);

            startActivity(intent);
        } else if (id == R.id.next_button) {
            Intent intent = new Intent(SNS_Login_Activity.this, RecyclerViewActivity_Refresh.class);

            startActivity(intent);
        } else if (id == R.id.login_move) {
            Intent intent = new Intent(SNS_Login_Activity.this, SplashActivity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Facebook 로그인 관련 메소드
     **/
    public void set_user_image(String user_id) {
        get_profile_image_task_facebook = new ImageTask(user_id);
        get_profile_image_task_facebook.execute(); //스레드 작업 실행//
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.apple.sample_app", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("KeyHash:", e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("KeyHash:", e.toString());
        }
    }

    /**
     * Facebook 관련 CustomLogin
     **/

    private void logoutFacebook() {
        mLoginManager.logOut(); //로그아웃.//

        profile_image.setImageResource(R.drawable.facebook_people_image);
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

                Toast.makeText(SNS_Login_Activity.this, "Token : " + token + "/ user id : " + user_id, Toast.LENGTH_SHORT).show();

                final GraphRequest request = GraphRequest.newMeRequest(
                        accessToken,
                        new GraphRequest.GraphJSONObjectCallback() {
                            //GraphAPI로 부터 데이터가 정상적으로 온 경우.//
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                // Application code
                                try {
                                    Profile profile = Profile.getCurrentProfile(); //현재의 프로필 정보를 받아온다.//

                                    //사용자 인증정보를 가져온다. 기본적인 JSON정보와 Profile을 이용.//
                                    //Exception방지로 opt로 불러온다.//
                                    id = object.optString("id");//페이스북 아이디값
                                    name = object.optString("name");//페이스북 이름
                                    profile_link = "" + profile.getLinkUri(); //프로파일을 이용해서 사용자주소 링크를 가져온다.//
                                    user_email = object.optString("email");

                                    Log.d("user email : ", user_email);

                                    if (name == null) {
                                        mStatusTextView.setText("null");
                                    } else {
                                        mStatusTextView.setText(name);
                                    }

                                    what_sns_click = 1;

                                    //id를 넘겨 이미지의 토큰값으로 활용//
                                    set_user_image(id);

                                } catch (FacebookException e) {
                                    Toast.makeText(SNS_Login_Activity.this, "사용자 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
                                }

                                // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다//
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

        //기존 제공해주는 로그인 버튼으로도 가능.//
        mLoginManager.logInWithReadPermissions(SNS_Login_Activity.this, Arrays.asList("email")); //이메일 획득 권한//
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

    class Google_ImageTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap;
        private String URL_Address = "";
        private boolean isCheck = false; //이미지가 처음엔 다운로드 실패했다는 가정//

        //URL주소를 셋팅하는 생성자.//
        public Google_ImageTask(String profile_url) {
            URL_Address = profile_url;
        }

        @Override
        protected void onPreExecute() {
            Log.i("image download file : ", URL_Address);
        }

        @Override
        protected Boolean doInBackground(Void... data) {
            //이미지 다운로드 작업 시작 및 네트워크 작업 진행//
            try {
                URL url = new URL(URL_Address); //연결한 URL주소 셋팅//

                try {
                    //네트워크 객체 선언.//
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect(); //연결//

                    InputStream is = conn.getInputStream(); //스트림 객체 선언//

                    Log.i("image value : ", "" + is);

                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                isCheck = true;
            }

            return isCheck;
        }

        @Override
        protected void onPostExecute(Boolean image_check) //메인 UI와 통신가능//
        {
            if (image_check == true) {
                profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                profile_image.setImageResource(R.drawable.not_image);
            }
        }
    }

    class ImageTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap;
        private String URL_Address = "";
        private boolean isCheck = false; //이미지가 처음엔 다운로드 실패했다는 가정//

        //URL주소를 셋팅하는 생성자.//
        public ImageTask(String user_token_id) {
            URL_Address = "https://graph.facebook.com/";
            URL_Address = URL_Address + user_token_id + "/";
            URL_Address = URL_Address + "picture";
        }

        @Override
        protected void onPreExecute() {
            Log.i("image download file : ", URL_Address);
        }

        @Override
        protected Boolean doInBackground(Void... data) {
            //이미지 다운로드 작업 시작 및 네트워크 작업 진행//
            try {
                URL url = new URL(URL_Address); //연결한 URL주소 셋팅//

                try {
                    //네트워크 객체 선언.//
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect(); //연결//

                    InputStream is = conn.getInputStream(); //스트림 객체 선언//

                    Log.i("image value : ", "" + is);

                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                isCheck = true;
            }

            return isCheck;
        }

        @Override
        protected void onPostExecute(Boolean image_check) //메인 UI와 통신가능//
        {
            if (image_check == true) {
                profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                profile_image.setImageResource(R.drawable.not_image);
            }
        }
    }

    class Twitter_ImageTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap;
        private String URL_Address = "";
        private boolean isCheck = false; //이미지가 처음엔 다운로드 실패했다는 가정//

        //URL주소를 셋팅하는 생성자.//
        public Twitter_ImageTask(String profile_url) {
            URL_Address = profile_url;
        }

        @Override
        protected void onPreExecute() {
            Log.i("image download file : ", URL_Address);
        }

        @Override
        protected Boolean doInBackground(Void... data) {
            //이미지 다운로드 작업 시작 및 네트워크 작업 진행//
            try {
                URL url = new URL(URL_Address); //연결한 URL주소 셋팅//

                try {
                    //네트워크 객체 선언.//
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect(); //연결//

                    InputStream is = conn.getInputStream(); //스트림 객체 선언//

                    Log.i("image value : ", "" + is);

                    bitmap = BitmapFactory.decodeStream(is);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            if (bitmap != null) {
                isCheck = true;
            }

            return isCheck;
        }

        @Override
        protected void onPostExecute(Boolean image_check) //메인 UI와 통신가능//
        {
            if (image_check == true) {
                profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                profile_image.setImageResource(R.drawable.not_image);
            }
        }
    }
}
