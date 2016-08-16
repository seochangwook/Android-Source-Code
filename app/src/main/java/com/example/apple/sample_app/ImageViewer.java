package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.sample_app.data.Trans_Data.Image_Trans;
import com.squareup.picasso.Picasso;

import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ImageViewer extends AppCompatActivity {
    public static final String EXTRA_IMAGE = "image";
    ImageView imageview;
    TextView image_name_text;
    String image_url;
    String image_content;
    Image_Trans image_trans;
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_viewer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        imageview = (ImageView) findViewById(R.id.image_data_view);
        image_name_text = (TextView) findViewById(R.id.image_data_content);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //넘어온 객체의 정보는 강제 캐스팅을 이용하여 설정.//
        image_trans = (Image_Trans) getIntent().getSerializableExtra(EXTRA_IMAGE); //직렬화 정보를 가져온다.//

        image_url = image_trans.get_download_image_url();
        image_content = image_trans.get_download_image_content();

        setData(image_url, image_content);
    }

    public void setData(String image_url, String image_content) {
        showpDialog();

        //Picasso를 적용.//
        Picasso.with(this)
                .load(image_url)
                .transform(new CropCircleTransformation())
                .into(imageview); //into로 보낼 위젯 선택.//

        image_name_text.setText(image_content);

        hidepDialog();
    }

    void showpDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hidepDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}
