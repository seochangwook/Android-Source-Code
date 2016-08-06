package com.example.apple.sample_app.view;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.apple.sample_app.JSON_Data.Product;
import com.example.apple.sample_app.JSON_Data.TStoreResult;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Full_A_News;
import com.example.apple.sample_app.data.Full_News;
import com.example.apple.sample_app.widget.Full_News_Adapter;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Full_News extends Fragment {
    private static final String TSTORE_URL = "http://apis.skplanetx.com/tstore/products?version=1&page=1&count=%s&searchKeyword=%s&order=%s";
    private static final String SORT_ACCURACY = "R";
    private static final String SORT_LATEST = "L";
    private static final String SORT_DOWNLOAD = "D";
    Full_News_Adapter mAdapter; //어댑터 정의.//
    Full_News full_news;
    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//

    public Fragment_Full_News() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_full_news, container, false);

        recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.rv_full_list);

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        //새로고침되어서 다시 리사이클뷰와 연동될 수 있도록 한다.//
        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mAdapter = new Full_News_Adapter(getActivity()); //액티비티의 자원을 넘긴다.//
        full_news = new Full_News();

        recyclerview.setAdapter(mAdapter); //어댑터 적용.//

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

        get_Data(); //최초 네트워크로 부터 데이터를 로드.//

        //init_Data();

        return view;
    }

    public void get_Data() {
        //네트워크 작업을 하기 위해서 AsyncTask사용.GSON에서 결과는 항상 json파싱의 최상단 클래스가 된다.//
        class TStoreSearchTask extends AsyncTask<String, Void, TStoreResult> {
            @Override
            public void onPreExecute() {
                showpDialog(); //작업시작을 알려주는 프로그래스바 다이얼로그 생성//
            }

            @Override
            protected TStoreResult doInBackground(String... datas) {
                //네트워크 연결 및 데이터 송수신 작업.//
                //우선적으로 쿼리에 '%s'를 채워주기 위해서 URL 파라메터를 설정.//
                String page_count = datas[0]; //execute()로 넘어온 첫번째 값을 의미.(인코딩 작업 필수)//
                String keyword = datas[1];
                String sort_option = datas[2];

                try {
                    //printf()랑 동일한 특징으로 "%s, %s, %s"이니 format에도 3개가 된다.//
                    String urlText = String.format(TSTORE_URL, URLEncoder.encode(page_count, "utf-8")
                            , URLEncoder.encode(keyword, "utf-8"), URLEncoder.encode(sort_option, "utf-8"));

                    URL url = new URL(urlText);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    //서버로 요청하는 Request를 정의.(기술스팩 문서에 정의)//
                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("appKey", "c92b74ce-0a01-30ff-986a-ab012d738cc4");

                    int code = urlConnection.getResponseCode();

                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream is = urlConnection.getInputStream(); //입력 스트림 객체를 얻어온다.//
                        BufferedReader br = new BufferedReader(new InputStreamReader(is)); //속도의 향상을 위해 버퍼를 추가.//

                        //GSON을 이용하여 json파싱.//
                        Gson gson = new Gson();

                        TStoreResult result = gson.fromJson(br, TStoreResult.class); //입력받은 json데이터를 TStoreResult의
                        //형식으로 맞추어 준다.(반드시 json의 데이터의 타입과 이름이 일치해야 한다.//

                        return result;
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }

            @Override
            protected void onPostExecute(TStoreResult tStoreResult) {
                super.onPostExecute(tStoreResult);

                //우리가 원하는 것은 Product배열과 배열의 사이즈를 넘기면 된다.//
                set_Data(tStoreResult.tstore.getProducts().getProduct(), tStoreResult.tstore.getProducts().getProduct().length);
                hidepDialog();
            }
        }

        TStoreSearchTask g = new TStoreSearchTask();

        //데이터를 받아온다.(네트워크 작업이므로 AsyncTask<>를 사용.)//
        g.execute("20", "RPG", SORT_LATEST); //매필할 키워드값를 넘겨준다.//
    }

    public void set_Data(Product[] product, int product_array_size) {
        //현재 넘어온 것은 배열에 대한 참조값이고 실제 배열을 이용할려면 Product List를 구현.//
        List<Product> items = new ArrayList<>();

        //배열을 모두 push한다.(Array.asList()사용)//
        items.addAll(Arrays.asList(product));

        //만들어진 배열을 가지고 기존 데이터 초기화 작업 시작.//
        for (int i = 0; i < product_array_size; i++) {
            Log.d("item name : ", "" + items.get(i).getName() + "/item image : " + items.get(i).getThumbnailUrl());
        }

        int count = 0;

        for (int i = 0; i < product_array_size; i++) {
            Full_A_News input_full_a_news = new Full_A_News();

            //json으로 파싱된 데이터를 가져온다.//
            input_full_a_news.set_item_name(items.get(i).getName());
            input_full_a_news.set_item_description(items.get(i).getDescription());
            input_full_a_news.set_item_tinyUrl(items.get(i).getTinyUrl());
            input_full_a_news.set_item_webUrl(items.get(i).getWebUrl());
            input_full_a_news.set_item_thumbnailUrl(items.get(i).getThumbnailUrl());

            full_news.full_a_newsList.add(input_full_a_news);

            mAdapter.set_Full_News(full_news);
            mAdapter.set_Full_News_count(1);
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

    //데이터 전체를 받아오고 리사이클뷰에 뿌려줄 각각의 배열로 저장한다//

    /*public void init_Data()
    {
        int news_count = 10;

        full_news.set_total_news_count(news_count); //카운터 저장.//

        mAdapter.set_Full_News(full_news);

        mAdapter.set_Full_News_count(news_count);

        Full_A_News input_full_a_news = new Full_A_News();

        input_full_a_news.set_item_name("바람의 나라");
        input_full_a_news.set_item_description("넥슨의 온라인 게임");
        input_full_a_news.set_item_tinyUrl("www.nexon.com");
        input_full_a_news.set_item_webUrl("www.nexon.com");
        input_full_a_news.set_item_thumbnailUrl("default.png");

        full_news.full_a_newsList.add(input_full_a_news);

        mAdapter.set_Full_News(full_news);
        mAdapter.set_Full_News_count(news_count+1);
    }*/
}
