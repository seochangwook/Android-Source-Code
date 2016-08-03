package com.example.apple.sample_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.sample_app.data.My_Category;
import com.example.apple.sample_app.view.LoadMoreView;
import com.example.apple.sample_app.widget.My_Category_Adapter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

public class My_Info_Activity extends AppCompatActivity {
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME_FACEBOOK = "user_name_facebook";
    private static final String KEY_USER_PROFILE_URL = "user_profile_url";
    private static final String KEY_USER_NAME_GOOGLE = "user_name_google";
    private static final String KEY_SNS_CATEGORY = "sns_category";

    final int REQ_CODE_SELECT_IMAGE = 100;
    public String user_id_facebook;
    public String user_name_facebook;
    public String user_profile_url_google;
    public String user_name_google;
    public int my_scrapt_count;
    public int my_followoing_count;
    public int my_follower_count;
    public My_Category_Adapter mAdapter; //어댑터//
    public My_Category my_category;
    View enroll_footerview;
    ImageView user_profile_image;
    TextView user_name_text, user_info_text;
    TextView scrapt_count_text, following_count_text, follower_count_text;
    Button profile_edit_button;
    FloatingActionButton enroll_category_floating_button;
    Button enroll_button;
    Button select_category_image;
    EditText input_category_name;
    ImageView sample_imageview;
    Bitmap image_bitmap; //이미지를 저장할 변수(포맷은 비트맵)//
    ImageTask get_profile_image_task; //계정 이미지 불러오기 작업//
    Google_ImageTask get_profile_image_task_google; //계정 이미지 불러오기 작업//
    String sns_category;
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN); //EditText등으로 인해서 포커스가 되면
        //UI를 위로 올리는 구조.//

        user_profile_image = (ImageView) findViewById(R.id.user_profile_image);
        user_name_text = (TextView) findViewById(R.id.user_name_text);
        user_info_text = (TextView) findViewById(R.id.my_info_text);
        scrapt_count_text = (TextView) findViewById(R.id.scrapt_count);
        following_count_text = (TextView) findViewById(R.id.follow_count);
        follower_count_text = (TextView) findViewById(R.id.follower_count);
        profile_edit_button = (Button) findViewById(R.id.edit_button);
        recyclerview_refresh = (FamiliarRefreshRecyclerView) findViewById(R.id.my_rv_category_list);
        enroll_category_floating_button = (FloatingActionButton) findViewById(R.id.enroll_category_button);

        //새로고침되어서 다시 리사이클뷰와 연동될 수 있도록 한다.//
        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(this)); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        /** FooterView 처리 **/
        enroll_footerview = getLayoutInflater().inflate(R.layout.enroll_category_footerview, null);

        enroll_button = (Button) enroll_footerview.findViewById(R.id.enroll_category_button);
        select_category_image = (Button) enroll_footerview.findViewById(R.id.input_category_image_button);
        input_category_name = (EditText) enroll_footerview.findViewById(R.id.input_category_name_edit);
        sample_imageview = (ImageView) enroll_footerview.findViewById(R.id.sample_imageview);

        input_category_name.setFocusable(true);

        //어댑터를 연결.//
        my_category = new My_Category();
        mAdapter = new My_Category_Adapter(this);
        recyclerview.setAdapter(mAdapter);

        setSupportActionBar(toolbar);

        Intent intent = getIntent();

        //후에 이 부분은 네트워크로 사용자 정보를 가져올 수 있다.//
        user_id_facebook = intent.getStringExtra(KEY_USER_ID);
        user_name_facebook = intent.getStringExtra(KEY_USER_NAME_FACEBOOK);
        user_profile_url_google = intent.getStringExtra(KEY_USER_PROFILE_URL);
        user_name_google = intent.getStringExtra(KEY_USER_NAME_GOOGLE);
        sns_category = intent.getStringExtra(KEY_SNS_CATEGORY); //1이면 페이스북, 2이면 구글//

        //Log.d("user id : ", user_id);
        //Log.d("user name : ", user_name);

        setProfile(); //프로필 설정.//

        /** Data refresh **/
        //사용자가 위에서 새로고침 할 경우//
        recyclerview_refresh.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        recyclerview_refresh.pullRefreshComplete();

                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //사용자가 아래쪽에서 새로고침//
        recyclerview_refresh.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        recyclerview_refresh.loadMoreComplete();

                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        profile_edit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(My_Info_Activity.this, "나의 정보 수정", Toast.LENGTH_SHORT).show();
            }
        });

        enroll_category_floating_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recyclerview.addFooterView(enroll_footerview);

                mAdapter.notifyDataSetChanged();
            }
        });

        enroll_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /** 해당 데이터 등록 시 네트워크 서버에도 데이터를 저장. **/
                Toast.makeText(My_Info_Activity.this, "카테고리 등록", Toast.LENGTH_SHORT).show();

                String input_category_name_str = input_category_name.getText().toString();

                My_Category new_category = new My_Category(); //새로운 카테고리를 추가하기 위해서 객체를 생성.//

                new_category.category_lock_option = false; //디폴트 보안조건은 false이다.//
                new_category.category_name = input_category_name_str;
                new_category.category_image = image_bitmap;

                my_category.my_category_list.add(new_category);
                mAdapter.set_Category(my_category);

                recyclerview.removeFooterView(enroll_footerview); //다시 푸터뷰에 있는 내용을 지운다.//

                mAdapter.notifyDataSetChanged();
            }
        });

        select_category_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
            }
        });

        //init_Data(); //초기 서버로 부터 카테고리의 정보를 가져오는 것.//
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_SELECT_IMAGE) //이미지를 가져오는 요청에 대해서 처리//
        {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    //Uri에서 이미지 이름을 얻어온다.
                    //String name_Str = getImageNameToUri(data.getData());

                    //이미지 데이터를 비트맵으로 받아온다.
                    image_bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                    //배치해놓은 ImageView에 set
                    sample_imageview.setImageBitmap(image_bitmap);

                    //Toast.makeText(getBaseContext(), "name_Str : "+name_Str , Toast.LENGTH_SHORT).show();
                } catch (FileNotFoundException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void setProfile() {
        /** 이 부분은 Intent의 값과 네트워크로부터 데이터를 가져온다. **/

        //이름 설정.//
        if (sns_category.equals("1")) {
            user_name_text.setText(user_name_facebook);
        } else if (sns_category.equals("2")) {
            user_name_text.setText(user_name_google);
        }

        if (user_info_text.getText().toString().equals("*")) {
            user_info_text.setText("자기소개를 입력하세요!!");
        }

        //임시적으로 값 저장.//
        my_scrapt_count = 45;
        my_followoing_count = 4;
        my_follower_count = 105;

        scrapt_count_text.setText("" + my_scrapt_count);
        following_count_text.setText("" + my_followoing_count);
        follower_count_text.setText("" + my_follower_count);

        if (sns_category.equals("1")) {
            set_user_image_facebook(user_id_facebook); //프로필 이미지 설정.//
        } else if (sns_category.equals("2")) {
            set_user_image_google(user_profile_url_google); //프로필 이미지 설정.//
        }
    }

    public void set_user_image_facebook(String user_id) {
        get_profile_image_task = new ImageTask(user_id);
        get_profile_image_task.execute(); //스레드 작업 실행//
    }

    public void set_user_image_google(String user_profile_url) {
        get_profile_image_task_google = new Google_ImageTask(user_profile_url);
        get_profile_image_task_google.execute(); //스레드 작업 실행//
    }

    class ImageTask extends AsyncTask<Void, Void, Boolean> {
        Bitmap bitmap; //이미지의 값은 Bitmap으로 다운로드 한다.//
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
                user_profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                user_profile_image.setImageResource(R.drawable.not_image);
            }
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
                user_profile_image.setImageBitmap(bitmap); //이미지 초기화//
            } else if (image_check == false) {
                user_profile_image.setImageResource(R.drawable.not_image);
            }
        }
    }
}
