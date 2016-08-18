package com.example.apple.sample_app;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.apple.sample_app.data.Trans_Data.Image_Trans;
import com.example.apple.sample_app.dialog.Keyword_dialog_activity;
import com.example.apple.sample_app.widget.Adapter.MyGroupAdapter;
import com.squareup.picasso.Picasso;

import java.io.File;

import jp.wasabeef.blurry.Blurry;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class ImageViewer extends AppCompatActivity {
    public static final String EXTRA_IMAGE = "image";
    private static final int RC_SINGLE_IMAGE = 2;
    private static final int RC_KEYWORD_CODE = 100; //정상응답을 받을 경우//
    private static final String RC_KEYWORD_KEY = "keyword";

    ImageView imageview;
    TextView image_name_text;
    String image_url;
    String image_content;
    Image_Trans image_trans;
    /**
     * Popup Menu Button
     **/
    ExpandableListView expandablelistview;
    MyGroupAdapter mAdapter;
    Switch category_switch;
    String path = null;
    PopupWindow mPopup_2;
    /**
     * Popup Item
     **/
    Button select_image_button;
    ImageView previce_imageview;
    String group_name[];
    String child_name[];
    int child_image_id[];
    File uploadFile = null; //이미지도 하나의 파일이기에 파일로 만든다.//
    int menu_select_count = 0;
    int menu_option = 0;
    Menu menu;
    private ProgressDialog pDialog; //직관적인 다이얼로그 사용(현재 진행률 보기)//
    private boolean blurred = false;

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

        //넘어온 객체의 정보는 강제 캐스팅을 이용하여 설정.//
        image_trans = (Image_Trans) getIntent().getSerializableExtra(EXTRA_IMAGE); //직렬화 정보를 가져온다.//

        image_url = image_trans.get_download_image_url();
        image_content = image_trans.get_download_image_content();

        setData(image_url, image_content);

        /** PopupWindow 생성 **/
        //팝업창을 가지고 있는 뷰를 만든다.//
        View popupView = getLayoutInflater().inflate(R.layout.popup_layout_bottom, null);

        /** PopupMenu Item id **/
        select_image_button = (Button) popupView.findViewById(R.id.select_gallery);
        previce_imageview = (ImageView) popupView.findViewById(R.id.previce_imageview);
        expandablelistview = (ExpandableListView) popupView.findViewById(R.id.expandableListView);
        category_switch = (Switch) popupView.findViewById(R.id.switch_category);

        mAdapter = new MyGroupAdapter(this);
        expandablelistview.setAdapter(mAdapter);

        set_ExpanList_Data();

        category_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    Toast.makeText(ImageViewer.this, "스위치 체크", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ImageViewer.this, "스위치 해제", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //리스트뷰 이벤트 처리.//
        expandablelistview.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(ImageViewer.this, "원하는 카테고리를 선택하세요", Toast.LENGTH_SHORT).show();
            }
        });

        //자식뷰가 선택되었을 경우.//
        expandablelistview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int groupPosition, int childPosition, long id) {
                Toast.makeText(ImageViewer.this, "선택된 카테고리 : " + child_name[childPosition], Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        expandablelistview.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(ImageViewer.this, "카테고리 목록 닫기", Toast.LENGTH_SHORT).show();
            }
        });

        //팝업화면 설정.//
        mPopup_2 = new PopupWindow(popupView, ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT, true);
        mPopup_2.setTouchable(true);
        mPopup_2.setOutsideTouchable(true);
        mPopup_2.setBackgroundDrawable(new BitmapDrawable(getResources(), (Bitmap) null));
        mPopup_2.setAnimationStyle(R.style.PopupAnimationBottom); //애니메이션 등록.//

        mPopup_2.getContentView().setFocusableInTouchMode(true);
        mPopup_2.getContentView().setFocusable(true);

        //팝업창 사라지는 이벤트 등록//
        mPopup_2.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                Blurry.delete((ViewGroup) findViewById(R.id.container_layout)); //블로 효과 제거.//
            }
        });

        /** PopupMenu Item event **/
        select_image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(ImageViewer.this, "이미지 선택", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                startActivityForResult(intent, RC_SINGLE_IMAGE);
            }
        });
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
                    Log.d("Single", "path : " + path);

                    uploadFile = new File(path);

                    Glide.with(this)
                            .load(uploadFile)
                            .into(previce_imageview); //into로 보낼 위젯 선택.//
                }
            }
        } else if (requestCode == RC_KEYWORD_CODE) {
            if (resultCode == RESULT_OK) {
                String select_keyword = data.getStringExtra("keyword");

                Toast.makeText(getApplicationContext(), "" + select_keyword + "로 이동", Toast.LENGTH_SHORT).show();

                //리사이클뷰의 스크롤을 키워드명을 기준으로 이동.//
            }
        }
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

    public void set_ExpanList_Data() {
        //데이터 초기화.(네트워크로 부터 데이터를 로드한다.)//
        group_name = new String[]{"Category"};
        child_name = new String[]{"사회이슈", "인물", "정치"};
        child_image_id = new int[]{R.drawable.apple_image, R.drawable.apple_image_2, R.drawable.apple_image_3};

        //이중for문으로 그룹 당 자식을 생성.//
        for (int group_index = 0; group_index < group_name.length; group_index++) {
            for (int child_index = 0; child_index < child_name.length; child_index++) {
                String groupname = group_name[group_index];
                String childname = child_name[child_index];
                int child_image = child_image_id[child_index];

                mAdapter.set_List_Data(groupname, childname, child_image);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.friend_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.friend_menu_1) {
            Blurry.with(ImageViewer.this)
                    .radius(25)
                    .sampling(2)
                    .async()
                    .animate(500)
                    .onto((ViewGroup) findViewById(R.id.container_layout));

            mPopup_2.showAtLocation(findViewById(R.id.top_popup_button), Gravity.BOTTOM, 0, 0);
        } else if (id == R.id.friend_menu_2) {
            Intent intent = new Intent(ImageViewer.this, Keyword_dialog_activity.class);

            startActivityForResult(intent, RC_KEYWORD_CODE);
        }

        return super.onOptionsItemSelected(item);
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
