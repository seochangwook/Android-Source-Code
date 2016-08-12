package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestCode.LogoutRequestCode;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.view.Fragment_Chatting_List;
import com.example.apple.sample_app.view.Fragment_friend_list;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainTabActivity extends AppCompatActivity {
    String user_email;
    String user_name;
    String user_id;
    NetworkManager manager;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private ProgressDialog pDialog;
    private Callback requestlogoutcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Toast.makeText(MainTabActivity.this, "네트워크 상태를 확인해주세요", Toast.LENGTH_SHORT).show();

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
                        startActivity(new Intent(MainTabActivity.this, SampleLoginActivity.class));

                        hidepDialog();

                        finish();
                    }
                });
            } else if (result_code == 2) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(MainTabActivity.this, "로그아웃 실패", Toast.LENGTH_SHORT).show();

                        hidepDialog();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_tab);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

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

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main_tab, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0: {
                    Fragment_friend_list friend_list_view = new Fragment_friend_list();

                    /*
                    /** Fragment로 값을 전달할 필요가 있을 경우 *
                    Bundle bundle = new Bundle(); //Fragment에게 값을 전달하기 위해서 Bundle사용.//

                    //Intent일 때는 액티비티간에 데이터 전달이였다. 마찬가지로 프래그먼트도 (key,value)로 구성 후 bundle을 이용한다.//
                    bundle.putString("SERVER_IP_ADDRESS_KEY", server_ip);
                    bundle.putInt("PORT_NUMBER_KEY", Integer.parseInt(server_port_number));

                    fragment_1.setArguments(bundle); //프래그먼트에게 인자들(아규먼트)을 전송할 준비를 한다.//
                    */

                    return friend_list_view;
                }

                case 1: {
                    Fragment_Chatting_List chatting_list = new Fragment_Chatting_List();

                    return chatting_list;
                }
            }

            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "친구목록";
                case 1:
                    return "채팅목록";
            }
            return null;
        }
    }
}
