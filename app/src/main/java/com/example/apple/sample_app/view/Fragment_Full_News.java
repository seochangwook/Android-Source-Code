package com.example.apple.sample_app.view;


import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.Toast;

import com.example.apple.sample_app.JSON_Data.RequestSuccess.Product;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.TStoreResult;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.TStore_Category;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.TStore_CategoryResult;
import com.example.apple.sample_app.R;
import com.example.apple.sample_app.data.Category_News;
import com.example.apple.sample_app.data.Full_A_News;
import com.example.apple.sample_app.data.Full_News;
import com.example.apple.sample_app.data.Game_News;
import com.example.apple.sample_app.widget.Category_News_Adapter;
import com.example.apple.sample_app.widget.Full_News_Adapter;
import com.example.apple.sample_app.widget.Game_News_Adapter;
import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
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
import java.util.concurrent.TimeUnit;

import cn.iwgang.familiarrecyclerview.FamiliarRecyclerView;
import cn.iwgang.familiarrecyclerview.FamiliarRefreshRecyclerView;
import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_Full_News extends Fragment {
    //서버 URL 주소//
    private static final String TSTORE_URL = "http://apis.skplanetx.com/tstore/products?version=1&page=1&count=%s&searchKeyword=%s&order=%s";
    private static final String SORT_ACCURACY = "R";
    private static final String SORT_LATEST = "L";
    private static final String SORT_DOWNLOAD = "D";
    private static final String TSTORE_CATEGORY_URL = "http://apis.skplanetx.com/11st/common/categories/?count=%s&page=1&sortCode=%s&option=&version=1";
    private static final String SORT_ACCURACY_CATEGORY = "CP";
    private static final String TSTORE_URL_REQUEST_2 = "http://apis.skplanetx.com/tstore/products?count=%s&order=%s&page=1&searchKeyword=%s&version=1";
    private static final String SORT_LATEST_2 = "L";
    ScrollView scrollview;
    //어댑터 정의.//
    Full_News_Adapter mAdapter;
    Category_News_Adapter category_news_adapter;
    Game_News_Adapter game_news_adapter;

    Full_News full_news;
    Category_News category_news;
    Game_News game_news;

    private FamiliarRefreshRecyclerView recyclerview_refresh; //초기화 가능한 리사이클뷰.//
    private FamiliarRefreshRecyclerView category_recyclerview_refresh;
    private FamiliarRefreshRecyclerView game_news_recyclerview_refresh;
    private FamiliarRecyclerView recyclerview; //초기화되는 리사이클뷰에 자원을 가지고 있는 리사이클뷰.//
    private FamiliarRecyclerView category_recyclerview;
    private FamiliarRecyclerView game_news_recyclerview;

    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private Callback callbackAfterGettingMessage = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) //연결실패.//
        {
            Toast.makeText(getActivity(), "Network Connection fail...", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException //연결성공//
        {
            String response_data = response.body().string();
            int response_code = response.code();

            Log.d("response message : ", response_data);
            Log.d("responser code : ", "" + response_code);

            if (response_code == HttpURLConnection.HTTP_OK) {
                //GSON을 이용하여 json파싱.//
                Gson gson = new Gson();

                TStoreResult result = gson.fromJson(response_data, TStoreResult.class); //입력받은 json데이터를 TStoreResult의
                //형식으로 맞추어 준다.(반드시 json의 데이터의 타입과 이름이 일치해야 한다.//

                set_game_news_Data(result.tstore.getProducts().getProduct(), result.tstore.getProducts().getProduct().length);
            }
        }
    };

    public Fragment_Full_News() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_fragment_full_news, container, false);

        recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.rv_full_list);
        category_recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.rv_category_list);
        game_news_recyclerview_refresh = (FamiliarRefreshRecyclerView) view.findViewById(R.id.rv_game_news_list);

        scrollview = (ScrollView) view.findViewById(R.id.scrollview);

        recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        recyclerview_refresh.setLoadMoreEnabled(true);

        category_recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        category_recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        category_recyclerview_refresh.setLoadMoreEnabled(true);

        game_news_recyclerview_refresh.setLoadMoreView(new LoadMoreView(getActivity())); //로딩화면을 보여주는 뷰 정의.//
        game_news_recyclerview_refresh.setColorSchemeColors(0xFFFF5000, Color.RED, Color.YELLOW, Color.GREEN);
        game_news_recyclerview_refresh.setLoadMoreEnabled(true);

        //새로고침되어서 다시 리사이클뷰와 연동될 수 있도록 한다.//
        category_recyclerview = category_recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        category_recyclerview.setItemAnimator(new DefaultItemAnimator());
        category_recyclerview.setHasFixedSize(true);

        game_news_recyclerview = game_news_recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        game_news_recyclerview.setItemAnimator(new DefaultItemAnimator());
        game_news_recyclerview.setHasFixedSize(true);

        recyclerview = recyclerview_refresh.getFamiliarRecyclerView(); //리사이클뷰의 자원을 얻어온다.//
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setHasFixedSize(true);

        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        mAdapter = new Full_News_Adapter(getActivity()); //액티비티의 자원을 넘긴다.//
        category_news_adapter = new Category_News_Adapter(getActivity());
        game_news_adapter = new Game_News_Adapter(getActivity());

        full_news = new Full_News();
        category_news = new Category_News();
        game_news = new Game_News();

        //어댑터 장착.//
        recyclerview.setAdapter(mAdapter); //어댑터 적용.//
        category_recyclerview.setAdapter(category_news_adapter);
        game_news_recyclerview.setAdapter(game_news_adapter);

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

        recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollview.requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        /** Data refresh **/
        //사용자가 위에서 새로고침 할 경우//
        category_recyclerview_refresh.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        category_recyclerview_refresh.pullRefreshComplete();

                        category_news_adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //사용자가 아래쪽에서 새로고침//
        category_recyclerview_refresh.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        category_recyclerview_refresh.loadMoreComplete();

                        category_news_adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //스크롤뷰안에서 리사이클뷰의 스크롤을 처리하기 위해서 한다.//
        category_recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollview.requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        /** Data refresh **/
        //사용자가 위에서 새로고침 할 경우//
        game_news_recyclerview_refresh.setOnPullRefreshListener(new FamiliarRefreshRecyclerView.OnPullRefreshListener() {
            @Override
            public void onPullRefresh() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "당겨서 새로고침 중...");

                        game_news_recyclerview_refresh.pullRefreshComplete();

                        game_news_adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        //사용자가 아래쪽에서 새로고침//
        game_news_recyclerview_refresh.setOnLoadMoreListener(new FamiliarRefreshRecyclerView.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.i("EVENT :", "새로고침 완료");

                        game_news_recyclerview_refresh.loadMoreComplete();

                        game_news_adapter.notifyDataSetChanged();
                    }
                }, 1000);
            }
        });

        game_news_recyclerview.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollview.requestDisallowInterceptTouchEvent(true);

                return false;
            }
        });

        get_Data(); //최초 네트워크로 부터 데이터를 로드.//
        get_category_news_Data(); //네트워크로 카테고리 뉴스 데이터 획득.//

        try {
            get_game_news_Data(); //네트워크로 게임 뉴스 데이터 획득.//
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

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

    private void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    public void get_category_news_Data() {
        class TSTore_CategoryTask extends AsyncTask<String, Void, TStore_CategoryResult> {
            @Override
            protected TStore_CategoryResult doInBackground(String... datas) {
                String page_count = datas[0];
                String sort_option = datas[1];

                try {
                    String urlText = String.format(TSTORE_CATEGORY_URL, URLEncoder.encode(page_count, "utf-8"),
                            URLEncoder.encode(sort_option, "utf-8"));

                    URL url = new URL(urlText);

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                    urlConnection.setRequestProperty("Accept", "application/json");
                    urlConnection.setRequestProperty("appKey", "c92b74ce-0a01-30ff-986a-ab012d738cc4");

                    int code = urlConnection.getResponseCode();

                    if (code == HttpURLConnection.HTTP_OK) {
                        InputStream is = urlConnection.getInputStream();
                        BufferedReader br = new BufferedReader(new InputStreamReader(is));

                        Gson gson = new Gson();

                        TStore_CategoryResult result = gson.fromJson(br, TStore_CategoryResult.class);

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
            protected void onPostExecute(TStore_CategoryResult result) {
                super.onPostExecute(result);

                //우리가 원하는 것은 Product배열과 배열의 사이즈를 넘기면 된다.//
                set_Data(result.get_Categoryresponse().get_tstore_children().get_tstore_category(), result.get_Categoryresponse().get_tstore_children().Category.length);
            }
        }

        TSTore_CategoryTask g = new TSTore_CategoryTask();

        //데이터를 받아온다.(네트워크 작업이므로 AsyncTask<>를 사용.)//
        g.execute("20", SORT_ACCURACY_CATEGORY); //매필할 키워드값를 넘겨준다.//
    }

    public void set_Data(TStore_Category[] category, int category_array_size) {
        //현재 넘어온 것은 배열에 대한 참조값이고 실제 배열을 이용할려면 Product List를 구현.//
        List<TStore_Category> items = new ArrayList<>();

        //배열을 모두 push한다.(Array.asList()사용)//
        items.addAll(Arrays.asList(category));

        //만들어진 배열을 가지고 기존 데이터 초기화 작업 시작.//
        for (int i = 0; i < category_array_size; i++) {
            Log.d("item name : ", "" + items.get(i).get_CategoryName() + "/item image : " + items.get(i).get_Category_Image());
        }

        for (int i = 0; i < category_array_size; i++) {
            Category_News input_category_news = new Category_News();

            input_category_news.set_category_imaegUrl(items.get(i).get_Category_Image());
            input_category_news.set_category_name(items.get(i).get_CategoryName());
            input_category_news.set_category_code(items.get(i).get_CategoryCode());

            category_news.category_news_array.add(input_category_news);

            category_news_adapter.set_Category_News(category_news);
        }
    }

    public void get_game_news_Data() throws UnsupportedEncodingException, MalformedURLException {
        OkHttpClient client = new OkHttpClient(); //OkHttp 매커니즘 적용.//

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        //Context context = null;

        //쿠키 생성.(앱을 제거 시 캐시도 삭제)//
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(getActivity()));
        builder.cookieJar(cookieJar);

        File cacheDir = new File(getActivity().getCacheDir(), "network"); //캐시 디렉터리 생성.//

        if (!cacheDir.exists()) {
            cacheDir.mkdir();
        }

        //캐시를 만들지 않으면 성능상의 문제가 발생.//
        Cache cache = new Cache(cacheDir, 10 * 1024 * 1024); //캐시사이즈 설정.//
        builder.cache(cache);

        //타임아웃 지정.//
        builder.connectTimeout(30, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        //클라이언트 객체에 저장.//
        client = builder.build();

        String page_count = "10";
        String keyword = "GAME";

        //HttpUrl을 가지고도 만들 수 있다.//
        //addQueryParamter는 순서 상관없지만 format은 상관있다.//
        String urlText = String.format(TSTORE_URL_REQUEST_2, URLEncoder.encode(page_count, "utf-8")
                , URLEncoder.encode(SORT_LATEST_2, "utf-8"), URLEncoder.encode(keyword, "utf-8"));

        URL url = new URL(urlText);

        //설정된 URL주소를 가지고 GET방식 호출//
        Request request = new Request.Builder()
                .addHeader("Accept", "application/json")
                .addHeader("appKey", "c92b74ce-0a01-30ff-986a-ab012d738cc4")
                .url(url)
                .build();

        client.newCall(request).enqueue(callbackAfterGettingMessage); //콜백메소드 등록.비동기일 경우 enqueue
        //동기일 경우 execute()//

        //만약 네트워크 접속이 제대로 이루어지지 않으면 OkHttp자체에서 다시 retry를 시도.//
    }

    public void set_game_news_Data(Product[] product, int product_array_size) {
        List<Product> items = new ArrayList<>();

        items.addAll(Arrays.asList(product)); //배열에 현재 획득한 값을 전달.//

        for (int i = 0; i < product_array_size; i++) {
            Log.d("game name : ", items.get(i).getName() + "/thumbnailUrl : " + items.get(i).getThumbnailUrl());
        }

        for (int i = 0; i < product_array_size; i++) {
            Game_News input_game_news = new Game_News();

            input_game_news.set_name(items.get(i).getName());
            input_game_news.set_description(items.get(i).getDescription());
            input_game_news.set_thumbnailUrl(items.get(i).getThumbnailUrl());
            input_game_news.set_webUrl(items.get(i).getWebUrl());
            input_game_news.set_tinyUrl(items.get(i).getTinyUrl());

            game_news.game_news_array.add(input_game_news);

            //현재 콜백메소드로부터 호출된 것이기에 UI를 변경할려고 하면 runOnUiThread를 사용.//
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    game_news_adapter.set_game_News(game_news);
                }
            });
        }
    }
}
