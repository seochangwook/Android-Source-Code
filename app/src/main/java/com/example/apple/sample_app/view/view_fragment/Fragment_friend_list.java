package com.example.apple.sample_app.view.view_fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.apple.sample_app.ChatActivity;
import com.example.apple.sample_app.JSON_Data.RequestCode.UserListRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.UserListRequest;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.UserListRequestResult;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Trans_Data.User;
import com.example.apple.sample_app.data.View_Data.Chat_Friend;
import com.example.apple.sample_app.view.LoadMoreView;
import com.example.apple.sample_app.widget.Adapter.Chat_FriendListAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_friend_list extends Fragment {
    Chat_Friend chat_friend;
    Chat_FriendListAdapter mAdapter;
    NetworkManager manager; //네트워크 매니저 생성.//
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private Callback requestfriendlistcallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //접속 실패의 경우.//
        {
            //네트워크 자체에서의 에러상황.//
            Log.d("ERROR Message : ", e.getMessage());
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String response_data = response.body().string();

            Gson gson = new Gson();

            UserListRequestCode result_code = gson.fromJson(response_data, UserListRequestCode.class);

            int request_code = result_code.get_request_code();

            if (request_code == 1) {
                Log.d("Message : ", response_data);

                //배열을 만들어 파싱된 결과를 받아옴.//
                UserListRequest request_userlist = gson.fromJson(response_data, UserListRequest.class);

                set_UserList_Data(request_userlist.getResult(), request_userlist.getResult().length);

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();
                    }
                });
            } else if (request_code == 2) {
                Log.d("Message : ", "네트워크 에러");

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();
                    }
                });
            }
        }
    };

    public Fragment_friend_list() {
        // Required empty publdic constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friend_list, container, false);
        view.setId(R.id.friend_fragment_view);

        recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.friend_listview);
        recyclerview_refresh.setId(R.id.friend_listview);

        recyclerview_refresh.setId(android.R.id.list); //android.R을 이용하여 시스템의 리스트 id를 확보(동적으로 변하는
        //id를 유지하기 위해서 사용)//

        Log.d("Fragment friend list : ", "onCreateView Call, id : " + recyclerview_refresh.getId());

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        chat_friend = new Chat_Friend();
        mAdapter = new Chat_FriendListAdapter(getActivity());

        //어댑터 장착.//
        recyclerview.setAdapter(mAdapter); //어댑터 적용.//


        //사용자가 위에서 새로고침 할 경우//
        recyclerview_refresh.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        recyclerview_refresh.pullRefreshComplete();

                        get_FriendList_Data(); //친구목록을 불러온다.//

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

                        get_FriendList_Data(); //친구목록을 불러온다.//

                        mAdapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        recyclerview.setOnItemClickListener(new FamiliarRecyclerView.OnItemClickListener() {
            @Override
            public void onItemClick(FamiliarRecyclerView familiarRecyclerView, View view, int position) {
                String username = chat_friend.chat_friend_list.get(position).get_username();
                String useremail = chat_friend.chat_friend_list.get(position).get_useremail();
                String userid = chat_friend.chat_friend_list.get(position).get_userid();

                Toast.makeText(getActivity(), "이름:" + username + "/메일:" + useremail + "/아이디;" + userid, Toast.LENGTH_SHORT).show();

                //객체를 전달하기 위해서 설정.//
                User user = new User();

                user.setEmail(useremail);
                user.setId(Long.parseLong(userid));
                user.setUserName(username);

                Intent intent = new Intent(getActivity(), ChatActivity.class);

                intent.putExtra(ChatActivity.EXTRA_USER, user); //객체를 전달.//

                startActivity(intent);
            }
        });

        get_FriendList_Data(); //친구목록을 불러온다.//

        return view;
    }

    public void get_FriendList_Data() {
        showpDialog();

        /** Network 설정 **/
        manager = NetworkManager.getInstance(); //할당된 객체 정보를 가져온다.//

        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("friendlist"); //path지정.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestfriendlistcallback);
    }

    public void set_UserList_Data(final UserListRequestResult request_result[], final int request_result_size) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                List<UserListRequestResult> items = new ArrayList<>();

                items.addAll(Arrays.asList(request_result));

                for (int i = 0; i < request_result_size; i++) {
                    Log.d("friend : ", "name : " + items.get(i).getUserName() + "/ email : " + items.get(i).getEmail());
                }

                for (int i = 0; i < request_result_size; i++) {
                    Chat_Friend input_chat_friend = new Chat_Friend();

                    input_chat_friend.set_useremail(items.get(i).getEmail());
                    input_chat_friend.set_username(items.get(i).getUserName());
                    input_chat_friend.set_userid("" + items.get(i).getId());

                    chat_friend.chat_friend_list.add(input_chat_friend);

                    mAdapter.set_Chat_FriendList(chat_friend);
                }
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
