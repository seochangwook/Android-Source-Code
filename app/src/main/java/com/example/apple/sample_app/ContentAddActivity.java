package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.apple.sample_app.JSON_Data.RequestCode.ContentUploadRequestCode;
import com.example.apple.sample_app.JSON_Data.RequestSuccess.RequestUploadContent;
import com.example.apple.sample_app.NetworkManage.NetworkManager;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ContentAddActivity extends AppCompatActivity {
    private static final int RC_SINGLE_IMAGE = 2;
    Button btn;
    EditText image_content;
    ImageView thumbnail_imageview;

    File uploadFile = null; //이미지도 하나의 파일이기에 파일로 만든다.//

    String path = null;

    NetworkManager manager; //네트워크 매니저 생성.//

    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private Callback requestimageuploadlistcallback = new Callback() {
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

            ContentUploadRequestCode result_code = gson.fromJson(response_data, ContentUploadRequestCode.class);

            int result = result_code.get_request_code();

            if (result == 1) {
                Log.d("data : ", "" + response_data);

                RequestUploadContent upload_request = gson.fromJson(response_data, RequestUploadContent.class);

                String id = "" + upload_request.getResult().getId();
                String writeid = "" + upload_request.getResult().getWriterid();
                String content = upload_request.getResult().getContent();
                String imageUrl = upload_request.getResult().getImageUrl();

                Log.d("Data : ", "id : " + id + "/writeid : " + writeid + "/content : " + content + "/imageUrl : " + imageUrl);

                runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();

                        finish();
                    }
                });
            } else if (result == 2) {
                Log.d("Message : ", "Network Fail");

                runOnUiThread(new Runnable() {
                    public void run() {
                        hidepDialog();
                    }
                });
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_add);

        image_content = (EditText) findViewById(R.id.edit_message);
        thumbnail_imageview = (ImageView) findViewById(R.id.image_picture);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        btn = (Button) findViewById(R.id.btn_get_image);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RC_SINGLE_IMAGE);
            }
        });

        btn = (Button) findViewById(R.id.btn_upload);

        btn.setEnabled(false);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = image_content.getText().toString();
                String image_url = path;

                Log.d("Data : ", "image url : " + image_url + " / content : " + content);

                upload_data(uploadFile, content);
            }
        });
    }

    public void upload_data(File upload_file, String image_content) {
        showpDialog();

        /** 이미지 전송을 위한 미디어 타입 정의 **/
        /** 이미지 전송과정 : MediaType정의 -> Url주소 만들기 -> MultipartBody 작성(FORM, create)
         * -> RequestBody에 적용(POST방식) -> Request 정의 -> Callback메소드 정의
         */
        MediaType jpeg = MediaType.parse("image/jpeg");

        /** Network 설정 **/
        manager = NetworkManager.getInstance(); //할당된 객체 정보를 가져온다.//

        OkHttpClient client = manager.getClient();

        /** POST방식의 프로토콜 요청 설정 **/
        /** URL 설정 **/
        HttpUrl.Builder builder = new HttpUrl.Builder();

        builder.scheme("https"); //스킴정의(Http / Https)
        builder.host("my-project-1-1470720309181.appspot.com"); //host정의.//
        builder.addPathSegment("upload"); //path지정.//

        /** MultipartBody 설정 **/
        MultipartBody.Builder multipart_builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("content", image_content); //Text타입.//

        if (upload_file != null) {
            //여러개 추가 시 "myFile"로 여러개의 addFormDataPart를 만들면 된다.//
            multipart_builder.addFormDataPart("myFile", upload_file.getName(),
                    RequestBody.create(jpeg, upload_file)); //create로 생성.//
        }

        /** RequestBody 설정 **/
        RequestBody body = multipart_builder.build(); //MultiPart로 빌드.//

        /** Request 설정 **/
        Request request = new Request.Builder()
                .url(builder.build())
                .post(body) //POST방식 적용.//
                .tag(this)
                .build();

        /** 비동기 방식(enqueue)으로 Callback 구현 **/
        client.newCall(request).enqueue(requestimageuploadlistcallback);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SINGLE_IMAGE) {
            if (resultCode == RESULT_OK) {
                Uri fileUri = data.getData();
                Cursor c = getContentResolver().query(fileUri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
                if (c.moveToNext()) {
                    path = c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
                    Log.i("Single", "path : " + path);

                    btn.setEnabled(true);

                    uploadFile = new File(path);

                    Glide.with(this)
                            .load(uploadFile)
                            .into(thumbnail_imageview); //into로 보낼 위젯 선택.//
                }
            }
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
