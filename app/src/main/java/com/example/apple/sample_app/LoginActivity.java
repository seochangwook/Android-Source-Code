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

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    private static final String PERMISSION = "publish_actions";

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

                                // new joinTask().execute(); //자신의 서버에서 로그인 처리를 해줍니다
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
