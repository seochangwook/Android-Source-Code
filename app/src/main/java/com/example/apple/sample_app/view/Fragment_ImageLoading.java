package com.example.apple.sample_app.view;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.ContentAddActivity;
import com.example.apple.sample_app.JSON_Data.RequestCode.ContentsRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.ContentsRequest;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.ContentsRequestResult;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.ImageData;
import com.example.apple.sample_app.widget.ImageSetAdapter;
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
public class Fragment_ImageLoading extends Fragment {
    ImageSetAdapter mAdapter;

    ImageData imageData;
    NetworkManager manager; //네트워크 매니저 생성.//
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private Callback requestcontenslistcallback = new Callback() {
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

            ContentsRequestCode contents_result = gson.fromJson(response_data, ContentsRequestCode.class);

            int result_code = contents_result.get_request_code();

            if (result_code == 2) {
                Log.d("Message : ", "Network ERROR");

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();
                    }
                });
            } else if (result_code == 1) {
                Log.d("Message : ", "" + response_data);

                ContentsRequest contentsRequest = gson.fromJson(response_data, ContentsRequest.class);

                set_Data(contentsRequest.getResult(), contentsRequest.getResult().length);
            }
        }
    };


    public Fragment_ImageLoading() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_image_loading, container, false);

        view.setId(R.id.image_layout_fragment);

        setHasOptionsMenu(true); //해당 화면으로 진입 시 메뉴를 보이게 한다(서로 다른 메뉴를 보일 경우 필요)//

        recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.image_recyclerview);
        recyclerview_refresh.setId(android.R.id.list);

        Log.d("Fragment ImageLoading : ", "onCreateView Call, id : " + recyclerview_refresh.getId());

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        //새로고침되어서 다시 리사이클뷰와 연동될 수 있도록 한다.//
        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        //어댑터 정의.//
        mAdapter = new ImageSetAdapter(getActivity());
        imageData = new ImageData();

        recyclerview.setAdapter(mAdapter);

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

        get_Image_Data();

        return view;
    }

    public void get_Image_Data() {
        showpDialog();

        /** Network 설정 **/
        manager = NetworkManager.getInstance(); //할당된 객체 정보를 가져온다.//

        OkHttpClient client = manager.getClient();

        /** GET방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("contents"); //path지정.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .tag(getActivity())
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestcontenslistcallback);
    }

    public void set_Data(final ContentsRequestResult result_request[], final int result_request_size) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                List<ContentsRequestResult> items = new ArrayList<>();

                items.addAll(Arrays.asList(result_request));

                for (int i = 0; i < result_request_size; i++) {
                    Log.d("Data : ", "content name : " + items.get(i).getContent() + "/file path : " + items.get(i).getImageUrl());
                }

                for (int i = 0; i < result_request_size; i++) {
                    ImageData input_image = new ImageData();

                    input_image.set_image_content(items.get(i).getContent());
                    input_image.set_image_Url(items.get(i).getImageUrl());

                    imageData.imagelist.add(input_image);

                    mAdapter.set_image_data(imageData);
                }

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();
                    }
                });
            }
        });
    }

    void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //메뉴의 생성은 MenuInflater이용//
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.image_tab_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)  //메뉴의 선택.//
    {
        switch (item.getItemId()) {
            case R.id.add_image: {
                Intent intent = new Intent(getContext(), ContentAddActivity.class);
                startActivity(intent);

                break;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
