package com.example.apple.sample_app;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.special.ResideMenu.ResideMenu;
import com.special.ResideMenu.ResideMenuItem;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String PERMISSION = "publish_actions";

    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";

    ShareDialog shareDialog; //공유기능을 위한 다이얼로그 생성//
    TextView user_id_text;
    TextView user_name_text;
    TextView user_profile_link;
    TextView user_gender;
    TextView user_birthday;
    ImageView imageview;
    LoginButton loginbutton; //페이스북 로그인 버튼//
    Button share_button; //공유버튼//
    Button next_button;
    String id, name, profile_link, gender, birthday;
    ImageTask get_profile_image_task; //계정 이미지 불러오기 작업//
    private ResideMenu resideMenu;
    private CallbackManager callbackManager; //세션연결 콜백관리자.//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //페이스북 세션 초기화는 setContentView()이전에 해준다.//
        FacebookSdk.sdkInitialize(getApplicationContext()); //초기 facebook연동을 하기 위해서 초기화//
        AppEventsLogger.activateApp(this);

        setContentView(R.layout.activity_main);

        user_id_text = (TextView) findViewById(R.id.user_id_text);
        user_name_text = (TextView) findViewById(R.id.user_name_text);
        imageview = (ImageView) findViewById(R.id.imageView);
        user_profile_link = (TextView) findViewById(R.id.user_email_text);
        user_gender = (TextView) findViewById(R.id.user_gender_text);
        user_birthday = (TextView) findViewById(R.id.user_birthday_text);

        loginbutton = (LoginButton) findViewById(R.id.facebook_login_button);
        share_button = (Button) findViewById(R.id.facebook_share_button);
        next_button = (Button) findViewById(R.id.next_button);

        printKeyHash(); //해시값 구하기//

        Toast.makeText(LoginActivity.this, "id : " + id + "/" + "name : " + name, Toast.LENGTH_SHORT).show();

        callbackManager = CallbackManager.Factory.create();

        loginbutton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            //Volley방식으로 JSON파싱//
            @Override
            public void onSuccess(LoginResult loginResult) {
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
                                    gender = object.optString("gender");
                                    birthday = object.optString("verified");

                                    user_id_text.setText("" + id);
                                    user_name_text.setText("" + name);
                                    user_profile_link.setText("" + profile_link);

                                    if (birthday.equals("")) {
                                        user_birthday.setText("정보없음");
                                    } else {
                                        user_birthday.setText(birthday);
                                    }

                                    if (gender.equals("male")) {
                                        user_gender.setText("남성");
                                    } else if (gender.equals("female")) {
                                        user_gender.setText("여성");
                                    }

                                    //id를 넘겨 이미지의 토큰값으로 활용//
                                    set_user_image(id);
                                } catch (FacebookException e) {
                                    Toast.makeText(LoginActivity.this, "사용자 정보를 불러올 수 없습니다", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(LoginActivity.this, "로그인을 취소 하였습니다!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(LoginActivity.this, "에러가 발생하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        //공유기능//
        share_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareDialog = new ShareDialog(LoginActivity.this); //객체 얻기//

                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Hello Facebook")
                        .setContentDescription(
                                "The 'Hello Facebook' sample  showcases simple Facebook integration")
                        .setContentUrl(Uri.parse("http://developers.facebook.com/android"))
                        .build();

                shareDialog.show(linkContent);
            }
        });

        next_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RecyclerViewActivity_Refresh.class);

                startActivity(intent);
            }
        });

        /** Reside Menu 구성 **/
        //리사이드 메뉴 구성//
        resideMenu = new ResideMenu(this); //액티비티에 겹쳐서 뿌려지므로 액티비티의 자원을 얻는다.//
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
                Toast.makeText(LoginActivity.this, "홉 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        item_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "나의 프로필 화면으로 이동", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(LoginActivity.this, My_Info_Activity.class);

                //페이스북에서 받아온 정보를 넘긴다. 객체를 넘길 시 Parceable사용.//
                intent.putExtra(KEY_USER_ID, id);
                intent.putExtra(KEY_USER_NAME, name);

                startActivity(intent);

                resideMenu.closeMenu(); //메뉴를 다시 닫는다.//
            }
        });

        item_calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "달력 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });

        item_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(LoginActivity.this, "설정 화면으로 이동", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        Log.d("myLog", "requestCode  " + requestCode);
        Log.d("myLog", "resultCode" + resultCode);
        Log.d("myLog", "data  " + data.toString());
    }

    private void printKeyHash() {
        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.apple.sns_connection", PackageManager.GET_SIGNATURES);
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

    public void set_user_image(String user_id) {
        get_profile_image_task = new ImageTask(user_id);
        get_profile_image_task.execute(); //스레드 작업 실행//
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        return resideMenu.dispatchTouchEvent(ev); //모션이벤트 등록//
    }

    @Override
    public void onClick(View view) {

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
            resideMenu.openMenu(ResideMenu.DIRECTION_LEFT);
        } else if (id == R.id.google_login) {
            Intent intent = new Intent(LoginActivity.this, SNS_Login_Activity.class);

            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
                imageview.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                imageview.setImageResource(R.drawable.not_image);
            }
        }
    }
}
