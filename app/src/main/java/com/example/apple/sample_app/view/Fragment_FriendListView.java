package com.example.apple.sample_app.view;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Friend;
import com.example.apple.sample_app.widget.FriendListAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_FriendListView extends Fragment {
    //파싱할 때 사용할 태그명//
    private static final String TAG_NAME = "name";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE = "phone";
    private static final String TAG_HOMEPHONE = "home";
    private static final String TAG_MOBILEPHONE = "mobile";
    String json_URL = "http://api.androidhive.info/volley/person_array.json"; //서버의 주소로 추후 설정.//
    FriendListAdapter mAdapter; //어댑터//
    View friend_header_view; //헤더뷰.//
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//

    public Fragment_FriendListView() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friend_list_view, container, false);

        recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.rv_list_friend);

        friend_header_view = getActivity().getLayoutInflater().inflate(R.layout.friend_header_view, null, false);

        mAdapter = new FriendListAdapter(getActivity()); //어댑터 생성.//

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        //어댑터는 일반 리사이클뷰에서 장착.//
        recyclerview.setAdapter(mAdapter);
        recyclerview.addHeaderView(friend_header_view, true);

        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

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

        /** Initialize Data **/
        getData(json_URL); //데이터를 전송받고 -> UI에 뿌려주는 개념//

        return view;
    }

    public void getData(String json_url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected void onPreExecute() {
                showpDialog(); //작업시작을 알려주는 프로그래스바 다이얼로그 생성//
            }

            @Override
            protected String doInBackground(String... strings) {
                //첫번째로 저장된 값이 json url이다.//
                String uri = strings[0];

                BufferedReader br = null; //버퍼라는 개념을 적용하여 입출력을 빠르게 한다.//
                HttpURLConnection urlConnection = null;
                URL url;

                try {
                    //정석의 네트워크 작업.HttpURLConnection방법 사용.//
                    /** 전송방식은 GET 사용 **/
                    url = new URL(uri); //URL 설정.//

                    urlConnection = (HttpURLConnection) url.openConnection(); //연결을 준비.//

                    //연결모드를 설정.//
                    urlConnection.setDoInput(true); //전송모드로 설정.//

                    //실제 연결이 이루어진다.//
                    br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream())); //버퍼를 씌워서 데이터를
                    //가져온다. POST로 전송시 getOutputStream() / OutputStream사용.//

                    String json; //전달받을 문자열을 가질 문자열 객체.//
                    StringBuilder sb = new StringBuilder();

                    //라인수만큼 String에 저장.//
                    while ((json = br.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim(); //공백을 제거하고 반환.//
                } catch (Exception exp) {
                    exp.printStackTrace();

                    return null;
                } finally {
                    urlConnection.disconnect();
                }
            }

            @Override
            protected void onPostExecute(String json_str) {
                //최종적으로 완성된 json값을 가지고 있다.//
                Log.d("json str : ", json_str);

                makeList(json_str);  //JSON -> LIST 변환작업//

                hidepDialog();
            }
        }

        GetDataJSON g = new GetDataJSON();

        //데이터를 받아온다.(네트워크 작업이므로 AsyncTask<>를 사용.)//
        g.execute(json_url); //연결할 주소를 넘겨준다.//
    }

    public void makeList(String json_str) {
        /**
         * [
         {
         "name" : "Ravi Tamada",
         "email" : "ravi8x@gmail.com",
         "phone" : {
         "home" : "08947 000000",
         "mobile" : "9999999999"
         }
         },
         {
         "name" : "Tommy",
         "email" : "tommy@gmail.com",
         "phone" : {
         "home" : "08946 000000",
         "mobile" : "0000000000"
         }
         }
         ]
         **/
        //처음 '['이니 JSONArray//
        Friend friend = new Friend(); //전체적으로 저장할 객체.//

        JSONArray friend_json_array = null;

        try {
            friend_json_array = new JSONArray(json_str); //초기 배열의 요소를 가져온다.//

            Log.d("json array size : ", "" + friend_json_array.length());

            //해당 개수를 가지고 JSONObject파싱을 한다.//
            for (int i = 0; i < friend_json_array.length(); i++) {
                JSONObject friend_object = friend_json_array.getJSONObject(i);

                String name = friend_object.getString(TAG_NAME);
                String email = friend_object.getString(TAG_EMAIL);

                //전화번호는 또 다른 객체이니 역시 따로 분리해서 사용(트리구조)//
                JSONObject phone_object = friend_object.getJSONObject(TAG_PHONE);

                String home_phone = phone_object.getString(TAG_HOMEPHONE);
                String mobile_phone = phone_object.getString(TAG_MOBILEPHONE);

                //객체 생성.//
                Friend input_friend = new Friend();

                input_friend.friend_name = name;
                input_friend.email_address = email;
                input_friend.home_phone = home_phone;
                input_friend.mobile_phone = mobile_phone;

                //해당 비트맵을 가져오게 되면 비트맵을 변경가능.//
                input_friend.friend_image = ContextCompat.getDrawable(getActivity(), R.drawable.facebook_people_image);
                //기본적으로 팔로잉 체크는 false//
                input_friend.is_follow_check = false;

                friend.friend_list.add(input_friend);
            }

            /** init Data **/
            for (int i = 0; i < friend.friend_list.size(); i++) {
                Log.d("name : ", friend.friend_list.get(i).friend_name);
                Log.d("email : ", friend.friend_list.get(i).email_address);
                Log.d("home : ", friend.friend_list.get(i).home_phone);
                Log.d("mobile : ", friend.friend_list.get(i).mobile_phone);
            }

            mAdapter.set_Friend(friend); //최종적으로 어댑터에 장착.//
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
